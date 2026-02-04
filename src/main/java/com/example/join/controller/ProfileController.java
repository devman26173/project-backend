package com.example.join.controller;

import com.example.join.entity.Profile;
import com.example.join.service.ProfileService;

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
			Model model
			){
				Profile profile = profileService.getOrCreateProfile(userId);
				model.addAttribute("profile", profile);
				return "profile";
	}
	
	//プロフィール編集ページを表示
	@GetMapping("/{userId}/edit")
	public String editForm(@PathVariable Long userId, Model model) {
	    Profile profile = profileService.getOrCreateProfile(userId);
	    model.addAttribute("profile", profile);
	    return "profile_edit";
	}
	
	//編集内容保存
	@PostMapping("/{userId}/edit")
	public String editProfile(
			@PathVariable Long userId,
			Profile formProfile
		) {
			profileService.updateProfile(userId, formProfile);
			return "redirect:/profile/" + userId;
	}

}