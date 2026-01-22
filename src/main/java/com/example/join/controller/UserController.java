package com.example.join.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    
    @GetMapping("/login")
    public String login() {
        return "user-login";  // login.html을 보여줌
    }
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("message", "태형 AI 👍");
        return "user-signup"; // templates/home.html
    }
}