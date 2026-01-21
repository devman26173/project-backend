package com.example.join.controller;
import com.example.join.service.FoodBoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.join.entity.FoodBoard;

@Controller
public class FoodBoardController {

    private final FoodBoardService foodBoardService;
	
	
	public FoodBoardController(FoodBoardService foodBoardService) {
		this.foodBoardService = foodBoardService;
	}

    @GetMapping("/board")
    public String home(Model model) {
    	model.addAttribute("boards", foodBoardService.findAll()); 
        return "foodboard";
    }
    // 게시글 작성 페이지
    @GetMapping("/board/write")
    public String write() {
        return "foodboard-write";
    }
    
    //게시글 저장 
    @PostMapping("/board/write")
    public String saveFood(FoodBoard foodBoard) {
    	foodBoardService.saveFood(foodBoard);
    	return "redirect:/board";
    }
}