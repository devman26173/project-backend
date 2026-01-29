package com.example.join.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.join.entity.User;
import com.example.join.service.SignupFormService;

@Controller
public class SignupController {

    private final SignupFormService signupFormService;

    public SignupController(SignupFormService signupFormService) {
        this.signupFormService = signupFormService;
    }

    // 会員登録画面の表示
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signupform"; // templates/signupform.html
    }

    // 登録処理の実行
    @PostMapping("/signup")
    public String signup(User user) {
        signupFormService.save(user);
        // 保存されたUserのIDを使ってプロフィールへ
        return "redirect:/profile/" + user.getUserId();
    }
}