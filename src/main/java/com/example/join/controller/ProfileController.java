package com.example.join.controller;

import com.example.join.entity.Comment;
import com.example.join.entity.FoodBoard;
import com.example.join.entity.Profile;
import com.example.join.entity.User;
import com.example.join.repository.CommentRepository;
import com.example.join.repository.FoodBoardRepository;
import com.example.join.service.ProfileService;
import com.example.join.service.UserService;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
	private final ProfileService profileService;
	private final FoodBoardRepository foodBoardRepository;
	private final CommentRepository commentRepository;

	public ProfileController(ProfileService profileService, FoodBoardRepository foodBoardRepository, CommentRepository commentRepository) {
		this.profileService = profileService;
		this.foodBoardRepository = foodBoardRepository;
		this.commentRepository = commentRepository;
	}

	// ログインしているユーザーのプロフィールページを保持セッション
	@GetMapping
	public String myProfile(HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null)
			return "redirect:/login";
		return "redirect:/profile/" + loginUser.getUserId();
	}

	// プロフィールを表示
	@GetMapping("/{userId}")
	public String showProfile(@PathVariable Long userId, Model model) {
		Profile profile = profileService.getByUserId(userId);

		// 26.02.06実装
		List<FoodBoard> boards = foodBoardRepository.findTop10ByUser_UserIdOrderByCreatedAtDesc(userId);
		
		// 26.02.10実装
		List<Comment> comments = commentRepository.findTop10ByUser_UserIdOrderByCreatedAtDesc(userId);
		
		
		model.addAttribute("profile", profile);
		model.addAttribute("boards", boards);
		model.addAttribute("comments", comments);
		
		return "profile";
	}

	// プロフィール編集ページを表示
	@GetMapping("/{userId}/edit")
	public String editForm(@PathVariable Long userId, Model model) {
		Profile profile = profileService.getByUserId(userId);
		model.addAttribute("profile", profile);
		return "profile_edit";
	}

	// 編集内容保存
	@PostMapping("/{userId}/edit")
	public String editProfile(@PathVariable Long userId, Profile formProfile) {
		profileService.updateProfile(userId, formProfile);
		return "redirect:/profile/" + userId;
	}
	//会員脱退処理機能
	@PostMapping("/{userId}/withdraw")
	public String withdraw(@PathVariable Long userId, HttpSession session) {
		userService. withdrawUser(userId);
		session.invalidate();
		return "redirect:/login";
	}

}