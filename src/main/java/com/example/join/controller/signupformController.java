package com.example.join.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class signupformController {

    @GetMapping("/signupform")
    public String signupform(Model model) {
        model.addAttribute("message", "태형 AI 👍");
        return "signupform"; // templates/home.html
    }
}