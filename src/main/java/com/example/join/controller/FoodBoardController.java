package com.example.join.controller;
import com.example.join.service.FoodBoardService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.join.entity.FoodBoard;
import com.example.join.entity.User;

@Controller
public class FoodBoardController {

    private final FoodBoardService foodBoardService;
	
	
	public FoodBoardController(FoodBoardService foodBoardService) {
		this.foodBoardService = foodBoardService;
	}

	@GetMapping("/board")
	public String home(@RequestParam(required = false) String region,
	                   @RequestParam(required = false) String prefecture,
	                   Model model) {
	    
	    // 지역 필터링
	    if (prefecture != null && !prefecture.isEmpty()) {
	        model.addAttribute("boards", foodBoardService.findByPrefecture(prefecture));
	        model.addAttribute("currentCategory", prefecture); // 현재 선택된 카테고리
	    } else if (region != null && !region.isEmpty()) {
	        model.addAttribute("boards", foodBoardService.findByRegion(region));
	        model.addAttribute("currentCategory", region); // 현재 선택된 카테고리
	    } else {
	        model.addAttribute("boards", foodBoardService.findAll());
	        model.addAttribute("currentCategory", "すべて"); // 기본값
	    }
	    
	    return "foodboard";
	}
    // 게시글 작성 페이지
    @GetMapping("/board/write")
    public String write(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login?returnUrl=/board/write";
        }
        return "foodboard-write";
    }
    
    //게시글 저장 
    @PostMapping("/board/write")
    public String saveFood(FoodBoard foodBoard, HttpSession session) {
    	User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login?returnUrl=/board/write";
        }
        foodBoard.setUser(loginUser);
        foodBoardService.saveFood(foodBoard);
        return "redirect:/board";
    }
    
    
    
    //게시글 상세 페이지 
    @GetMapping("/board/view/{id}")
    public String viewBoard(@PathVariable Long id, Model model) {
    	model.addAttribute("board", foodBoardService.findById(id));
    	return "foodboard-view";
    }
    
    //게시글 수정 페이지 
    @GetMapping("/board/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("board", foodBoardService.findById(id));
    return "foodboard-edit";
    }
    
    //게시글 수정 처리 
    @PostMapping("/board/edit/{id}")
    public String updateBoard(@PathVariable Long id, FoodBoard foodBoard, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        FoodBoard existingBoard = foodBoardService.findById(id);
        foodBoard.setUser(existingBoard.getUser());
        
        foodBoardService.updateBoard(id, foodBoard);
        return "redirect:/board/view/" + id;
    }
    
    //게시글 삭제 처리 
    @PostMapping("/board/delete/{id}")
    public String deleteBoard(@PathVariable Long id) {
    	foodBoardService.deleteBoard(id);
    	return "redirect:/board";
    }
   
}