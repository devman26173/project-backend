package com.example.join.controller;

import com.example.join.entity.FoodBoard;
import com.example.join.service.FoodBoardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final FoodBoardService foodBoardService;
    private final boolean quickViewEnabled;

    public HomeController(FoodBoardService foodBoardService,
                          @Value("${app.dev.quick-view-on-start:false}") boolean quickViewEnabled) {
        this.foodBoardService = foodBoardService;
        this.quickViewEnabled = quickViewEnabled;
    }

    @GetMapping("/")
    public String home(Model model) {
        if (quickViewEnabled) {
            return foodBoardService.findLatest()
                    .map(board -> "redirect:/board/view/" + board.getId())
                    .orElse("redirect:/board");
        }
    	List<String> prefectures = List.of("北海道・東北","関東","中部","近畿","中国・四国","九州・沖縄");
		model.addAttribute("prefectures", prefectures);
    	model.addAttribute("message", "Hello Thymeleaf!");
        return "home"; 
    }

    @GetMapping("/boards")
    public String boards(Model model) {
        Map<String, FoodBoard> regionPosts = foodBoardService.getFirstPostByRegion();
        model.addAttribute("regionPosts", regionPosts);
        return "boards";
    }
    
    public void prefecture(Model model) {		
		List<String> prefectures = List.of("北海道・東北","関東","中部","近畿","中国・四国","九州・沖縄");
		model.addAttribute("prefectures", prefectures);
	}
    
}
