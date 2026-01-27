package com.example.join.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.join.entity.User;
import com.example.join.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    private final UserService userService;
	
    public UserController(UserService userService) {
		this.userService = userService;
	}
    
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String returnUrl, Model model) {
        model.addAttribute("returnUrl", returnUrl);
        return "user-login";  // login.html을 보여줌
    }
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("message", "태형 AI 👍");
        return "user-signup"; // templates/home.html
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    @PostMapping("/signup")
    public String signupSubmit(
	    @RequestParam String username,
	    @RequestParam String name,
	    @RequestParam String password,
	    @RequestParam String passwordConfirm,
	    @RequestParam String region,
	    @RequestParam String prefecture,
	    Model model
    ) {
    	//비밀번호 확인
    	if (!password.equals(passwordConfirm)) {
    		model.addAttribute("error","パスワードが一致しません。");
    		return "user-signup";
    	}
    	//회원가입 처리
    	userService.registerUser(username, name, password, region, prefecture);
    	//로그인 페이지로 이동
    	return "redirect:/login" ;
    }
    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String returnUrl,
            HttpSession session,
            Model model) {
    	User user = userService.login(username, password);
    if(user != null) {
    	return "redirect:/board";
    }else {
    	model.addAttribute("error", "IDまたはパスワードが一致しません");
    	model.addAttribute("returnUrl", returnUrl);
    	return "user-login";
    }
}
}