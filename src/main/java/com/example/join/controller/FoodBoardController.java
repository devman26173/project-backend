package com.example.join.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FoodBoardController {

    @GetMapping("/board")
    public String home(Model model) {
        model.addAttribute("message", "제 도쿄 맛집 게시판을 소개합니다🥗");
        return "foodboard"; // templates/home.html
    }
}
