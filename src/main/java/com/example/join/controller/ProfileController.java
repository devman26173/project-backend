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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	private final ProfileService profileService;

	public ProfileController(ProfileService profileService) {
		this.profileService = profileService;
	}
	
	/**
	 * 인증 및 권한 검증 헬퍼 메서드
	 * @return 로그인한 사용자 (검증 실패 시 null)
	 */
	private User validateUserAccess(HttpSession session, Long userId, RedirectAttributes redirectAttributes) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return null;
		}
		if (!loginUser.getUserId().equals(userId)) {
			redirectAttributes.addFlashAttribute("error", "本人のプロフィールのみ編集できます");
			return null;
		}
		return loginUser;
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
	public String editForm(@PathVariable Long userId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		User loginUser = validateUserAccess(session, userId, redirectAttributes);
		if (loginUser == null) {
			User sessionUser = (User) session.getAttribute("loginUser");
			if (sessionUser == null) {
				return "redirect:/login?returnUrl=/profile/" + userId + "/edit";
			}
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
			RedirectAttributes redirectAttributes
		) {
		User loginUser = validateUserAccess(session, userId, redirectAttributes);
		if (loginUser == null) {
			User sessionUser = (User) session.getAttribute("loginUser");
			if (sessionUser == null) {
				return "redirect:/login?returnUrl=/profile/" + userId + "/edit";
			}
			return "redirect:/profile/" + userId;
		}
		
		profileService.updateProfile(userId, formProfile);
		return "redirect:/profile/" + userId;
	}

}