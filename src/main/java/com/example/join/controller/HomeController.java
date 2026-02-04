package com.example.join.controller;

import com.example.join.entity.FoodBoard;
import com.example.join.service.FoodBoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class HomeController {

    private final FoodBoardService foodBoardService;

    public HomeController(FoodBoardService foodBoardService) {
        this.foodBoardService = foodBoardService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Hello Thymeleaf!");
        return "home"; // templates/home.html
    }

    @GetMapping("/boards")
    public String boards(Model model) {
        Map<String, FoodBoard> regionPosts = foodBoardService.getFirstPostByRegion();
        model.addAttribute("regionPosts", regionPosts);
        return "boards";
    }
}