package com.example.join.controller;

import com.example.join.entity.Profile;
import com.example.join.entity.User;
import com.example.join.service.ProfileService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	private final ProfileService profileService;

	public ProfileController(ProfileService profileService) {
		this.profileService = profileService;
	}

	//プロフィールを表示
	@GetMapping("/{userId}")
	public String showProfile(
			@PathVariable Long userId,
			Model model,
			HttpSession session
			){
				Profile profile = profileService.getByUserId(userId);
				model.addAttribute("profile", profile);
				
				// 로그인한 사용자 정보 전달 (편집 버튼 표시 여부 등에 활용)
				User loginUser = (User) session.getAttribute("loginUser");
				model.addAttribute("loginUser", loginUser);
				
				return "profile";
	}
	
	//プロフィール編集ページを表示
	@GetMapping("/{userId}/edit")
	public String editForm(@PathVariable Long userId, Model model, HttpSession session) {
		// 로그인 확인
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login?returnUrl=/profile/" + userId + "/edit";
		}
		
		// 본인 확인
		if (!loginUser.getUserId().equals(userId)) {
			model.addAttribute("error", "本人のプロフィールのみ編集できます");
			return "redirect:/profile/" + userId;
		}
		
	    Profile profile = profileService.getByUserId(userId);
	    model.addAttribute("profile", profile);
	    return "profile_edit";
	}
	
	
	
	
	//編集内容保存
	@PostMapping("/{userId}/edit")
	public String editProfile(
			@PathVariable Long userId,
			Profile formProfile,
			HttpSession session,
			Model model
		) {
		// 로그인 확인
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login?returnUrl=/profile/" + userId + "/edit";
		}
		
		// 본인 확인
		if (!loginUser.getUserId().equals(userId)) {
			model.addAttribute("error", "本人のプロフィールのみ編集できます");
			return "redirect:/profile/" + userId;
		}
		
		profileService.updateProfile(userId, formProfile);
		return "redirect:/profile/" + userId;
	}

}