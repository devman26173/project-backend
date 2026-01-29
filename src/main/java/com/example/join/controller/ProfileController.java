package com.example.join.controller;

import com.example.join.entity.Profile;
import com.example.join.service.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
			Model model
	) {
		Profile profile = profileService.findByUserId(userId);
		model.addAttribute("profile", profile);
		return "profile/profile";
	}
	
	
	//編集画面
	@GetMapping("/{userId}/edit")
	public String edit(
			@PathVariable Long userId,
			Model model
	) {
		Profile profile = profileService.findByUserId(userId);
		model.addAttribute("profile", profile);
		return "profile/profile_edit";
	}

	//リダイレクト
	@PostMapping("/{userId}/edit")
	public String update(
			@PathVariable Long userId,
			Profile profile
	) {profileService.update(userId, profile);
		return "redirect:/profile/" + userId;
	}
}