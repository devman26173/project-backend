package com.example.join.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.join.entity.Post;

@Controller
public class PostController {
	
	private Post post = new Post(); //임시 (DB 대신)
	
	public PostController() {
		post.setId(1L);
		post.setContent("첫 게시글");
		post.setLikeCount(0);
		post.setLikedByMe(false);
		
	}

	@GetMapping("/post")
	public String post(Model model) {
		model.addAttribute("post", post);
		return "post"; // templates/home.html
	}
	
	@PostMapping("/post/like")
	public String toggleLike() {
		if (post.isLikedByMe()) {
		    post.setLikeCount(post.getLikeCount() - 1);
		    post.setLikedByMe(false);
		} else {
		    post.setLikeCount(post.getLikeCount() + 1); // 👍 likeCount 증가
		    post.setLikedByMe(true);                    // 👍 boolean 설정
		}

		return "redirect:/post";
	}
}
