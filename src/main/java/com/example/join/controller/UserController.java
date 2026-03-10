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
    public String login(@RequestParam(required = false) 
    	String returnUrl, HttpSession session, Model model) {
    	//로그인이 되어있는지 확인
    	User loginUser = (User) session.getAttribute("loginUser");
    	//로그인 상태면 board로 이동
    	if(loginUser != null) {
    		return "redirect:/board";
    	}
        model.addAttribute("returnUrl", returnUrl);
        return "user-login";
    }

    @GetMapping("/signup")
    public String signup(
         @RequestParam(required = false) String returnUrl,  // ✅ 추가
         HttpSession session, Model model) {
    	//로그인이 되어있는지 확인
    	User loginUser = (User) session.getAttribute("loginUser");
    	//로그인 상태면 board로 이동
    	if(loginUser != null) {
    		return "redirect:/board";
    	}
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
    	//비밀번호 확인
        if (!password.equals(passwordConfirm)) {
            model.addAttribute("error","パスワードが一致しません。");
            model.addAttribute("returnUrl", returnUrl);
            
            //에러 나도 입력받은 값 유지(패스워드 제외)
            model.addAttribute("username", username);
            model.addAttribute("name", name);
            model.addAttribute("region", region);
            model.addAttribute("prefecture", prefecture);
            
            return "user-signup";
        }
        //중복 체크 에러 잡아내기
        try {
        	userService.registerUser(username, name, password, region, prefecture);
        }catch(IllegalArgumentException e) {
        	//중복 ID면 에러메시지 보여주기
        	model.addAttribute("error","このIDはすでに使用されています。");
        	model.addAttribute("returnurl", returnUrl);
        	
        	model.addAttribute("username", username);
        	model.addAttribute("name", name);
        	model.addAttribute("region", region);
        	model.addAttribute("prefecture", prefecture);
        	
        	return "user-signup";
        }
        
        
        // returnUrl이 있으면 login 페이지로 리다이렉트할 때 함께 전달
        if (returnUrl != null && !returnUrl.isEmpty()) {
            return "redirect:/login?returnUrl=" + returnUrl;
        }
        return "redirect:/login";
    }
    //비밀번호 수정 페이지 보여주기
    @GetMapping("/change-password")
    public String changePassword(HttpSession session) {
    	//로그인 안했으면 로그인 페이지로
    	User loginUser = (User) session.getAttribute("loginUser");
    	if (loginUser == null) {
    		return "redirect:/login";
    	}
    	return "user-change-password";
    }
    
    //비밀번호 수정 처리 (POST)
    @PostMapping("/change-password")
    public String changePasswordSubmit(
    		@RequestParam String newPassword,
    		@RequestParam String newPasswordConfirm,
    		HttpSession session,
    		Model model) {
    	User loginUser = (User) session.getAttribute("loginUser");
    	if (loginUser == null) {
    		return "redirect:/login";
    	}
    	//새 비밀번호와 일치하는지 확인
    	if (!newPassword.equals(newPasswordConfirm)) {
    		model.addAttribute("error", "新しいパスワードが一致しません。");
    		return "user-change-password";
    	}
    	//db에 새 비밀번호 암호화해서 저장
    	userService.changePassword(loginUser.getUserId(), newPassword);
    	//비밀번호가 바뀌었으니 세션 무효화 & 새 비밀번호로 다시 로그인하게 만들기
    	//보안상 비밀번호 변경 후 재로그인이 일반적
    	session.invalidate();
    	return "redirect:/login";
    }
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
            
            // returnUrl이 있으면 해당 페이지로 리다이렉트
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
    
    //회원탈퇴
    @GetMapping("/withdraw")
    public String withdraw(HttpSession session, Model model) {
    	User loginUser = (User) session.getAttribute("loginUser");
    	if (loginUser == null) { //로그인 안했으면 로그인 페이지로
    		return "redirect:/login";
    	}
		return "user-withdraw";
    }
    @PostMapping("/withdraw")
    public String withdrawSubmit(HttpSession session) {
    	User loginUser = (User) session.getAttribute("loginUser");
    	if (loginUser == null) {
    		return "redirect:/login";
    	}
    	//회원탈퇴 처리
    	userService.withdrawUser(loginUser.getUserId());
    	//세션삭제
    	session.invalidate();
    	//로그인 페이지로
    	return "redirect:/login";
    }
    @GetMapping("/verify-password")
    public String verifyPassword(HttpSession session) {
    	//로그인 안했으면 로그인 페이지로 이동
    	User loginUser =(User) session.getAttribute("loginUser");
    	if (loginUser == null) {
    		//로그인 안했으면 로그인 페이지로
    		return "redirect:/login";
    	}
    	return "user-verify-password";
    }
    //비밀번호 확인(Post) 입력받은 비밀번호 맞으면 ->비밀번호 수정페이지로 이동
    @PostMapping("/verify-password")
    public String verifyPasswordSubmit(
    	@RequestParam String password, HttpSession session, Model model){
    	User loginUser = (User) session.getAttribute("loginUser");
    	if(loginUser == null) {
    		return "redirect:/login";
    	}
    	//UserService의 verifyPassword로 현재 비밀번호 확인
    	if(userService.verifyPassword(loginUser, password)) {
    		//비밀번호 일치 => 수정페이지로 이동
    		return "redirect:/change-password";
    	}else {
    		//틀리면 에러메시지와 함께 다시 확인 페이지
    		model.addAttribute("error", "パスワードが一致しません。");
    		return "user-verify-password";
    	}
    }
}
