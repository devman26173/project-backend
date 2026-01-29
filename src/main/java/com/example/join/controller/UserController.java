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
        return "user-login";
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
        @RequestParam(required = false) String returnUrl,
        Model model
    ) {
        if (!password.equals(passwordConfirm)) {
            model.addAttribute("error","パスワードが一致しません。");
            model.addAttribute("returnUrl", returnUrl);
            return "user-signup";
        }
        userService.registerUser(username, name, password, region, prefecture);
        return "redirect:/login";
    }
    
    // ✅ 이 메서드 추가 (빠져있었어요!)
    @PostMapping("/login")
    public String loginSubmit(
        @RequestParam String username,
        @RequestParam String password,
        @RequestParam(required = false) String returnUrl,
        HttpSession session,
        Model model
    ) {
        System.out.println("=== 로그인 시도 ===");
        System.out.println("username: " + username);
        
        User user = userService.login(username, password);
        
        if(user != null) {
            System.out.println("✅ 로그인 성공!");
            session.setAttribute("loginUser", user);
            
            if(returnUrl != null && !returnUrl.isEmpty()) {
                return "redirect:" + returnUrl;
            }
            return "redirect:/board";
        } else {
            System.out.println("❌ 로그인 실패!");
            model.addAttribute("error", "IDまたはパスワードが一致しません");
            model.addAttribute("returnUrl", returnUrl);
            return "user-login";
        }
    }
    
    @PostMapping("/logout")
    public String processLogout(HttpSession session) {
    	if (session.getAttribute("loginUser") == null) {
    		return "redirect:/login";
    	}
    	userService.logout(session);
    	return "redirect:/login";
    }
}
