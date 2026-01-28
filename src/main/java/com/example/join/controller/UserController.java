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
    public String signup(
         @RequestParam(required = false) String returnUrl,  // ✅ 추가
         Model model) {
     model.addAttribute("message", "태형 AI 👍");
     model.addAttribute("returnUrl", returnUrl);  // ✅ 추가
     return "user-signup";
 }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    // ✅ 수정: 회원가입 후 returnUrl 처리
    @PostMapping("/signup")
    public String signupSubmit(
	    @RequestParam String username,
	    @RequestParam String name,
	    @RequestParam String password,
	    @RequestParam String passwordConfirm,
	    @RequestParam String region,
	    @RequestParam String prefecture,
	    @RequestParam(required = false) String returnUrl,  // ✅ 추가
	    Model model
    ) {
    	//비밀번호 확인
    	if (!password.equals(passwordConfirm)) {
    		model.addAttribute("error","パスワードが一致しません。");
    		model.addAttribute("returnUrl", returnUrl);  // ✅ 추가
    		return "user-signup";
    	}
    	//회원가입 처리
    	userService.registerUser(username, name, password, region, prefecture);
    	
    	// ✅ 수정: returnUrl이 있으면 로그인 페이지에 전달
        if(returnUrl != null && !returnUrl.isEmpty()) {
            return "redirect:/login?returnUrl=" + returnUrl;
        }
    	
    	//로그인 페이지로 이동
    	return "redirect:/login" ;
    }
    
    // ✅ 수정: 로그인 후 returnUrl 처리
    @PostMapping("/login")
    public String loginSubmit(
    		@RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String returnUrl,
            HttpSession session,
            Model model) {
    	
    	User user = userService.login(username, password);
    
    	if(user != null) {
    	// 세션에 사용자 정보 저장
        session.setAttribute("loginUser", user);
        
        // ✅ 수정: returnUrl 우선 처리
        if(returnUrl != null && !returnUrl.isEmpty()) {
            return "redirect:" + returnUrl;
        }
        
        // returnUrl이 없으면 board로
        return "redirect:/board";
    } else {
        model.addAttribute("error", "IDまたはパスワードが一致しません");
        model.addAttribute("returnUrl", returnUrl);
        return "user-login";
    }
}
}
