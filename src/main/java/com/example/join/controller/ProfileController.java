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
	
	@GetMapping("")
    public String showMyProfile(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        Profile profile = profileService.findByUserId(loginUser.getUserId());
        model.addAttribute("profile", profile);
        
        // templates/profile.html を直接見るように修正
        return "profile"; 
    }
	
	@GetMapping("/{userId}")
    public String showProfile(@PathVariable Long userId, Model model) {
        Profile profile = profileService.findByUserId(userId);
        model.addAttribute("profile", profile);
        
        // ここも修正
        return "profile";
	}

	@GetMapping("/{userId}/edit")
	public String edit(@PathVariable Long userId, Model model) {
		Profile profile = profileService.findByUserId(userId);
		model.addAttribute("profile", profile);
		return "profile-edit"; // templates/home.html
	}

	@PostMapping("/{userId}/edit")
	public String update(@PathVariable Long userId, Profile profile) {
		profileService.update(userId, profile);
		// userIdの前に / を入れないと正しく遷移しません
		return "redirect:/profile/" + userId;

	}

}