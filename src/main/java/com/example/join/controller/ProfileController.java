package com.example.join.controller;

import com.example.join.entity.Profile;
import com.example.join.repository.ProfileRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

	private final ProfileRepository profileRepository;

	public ProfileController(ProfileRepository profileRepository) {
		this.profileRepository = profileRepository;
	}

	@GetMapping("/profile")
	public String profile(Model model) {

		Profile profile = profileRepository
				.findById(1L)
				.orElse(null);

		model.addAttribute("profile", profile);
		return "profile"; // templates/home.html
	}

}
