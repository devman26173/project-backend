package com.example.join.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.join.entity.Post;
import com.example.join.entity.Post.Comment;
import java.time.LocalDateTime;

@Controller
public class PostController {
	
	private Post post = new Post(); //임시 (DB 대신)
	private Long commentIdCounter = 1L;	//댓글 ID 생성용
	
	public PostController() {
		post.setId(1L);
		post.setContent("첫 게시글");
		post.setLikeCount(0);
		post.setLikedByMe(false);
	
		//샘플 댓글 추가
		Comment sampleComment = new Comment(1L, "첫 댓글입니다!", "유저1");
		sampleComment.setCreatedAt(LocalDateTime.now().minusMinutes(30));
		post.addComment(sampleComment);
		commentIdCounter = 2L;
	}

	@GetMapping("/post")
	public String post(Model model) {
		model.addAttribute("post", post);
		model.addAttribute("commentCount", post.getComments().size());
		return "post"; // templates/post.html
	}
	
	@PostMapping("/post/like")
	public String toggleLike() {
		if (post.isLikedByMe()) {
		    post.setLikeCount(post.getLikeCount() - 1);
		    post.setLikedByMe(false);
		} else {
		    post.setLikeCount(post.getLikeCount() + 1);
		    post.setLikedByMe(true);
		}
		return "redirect:/post";
	}
	
	//댓글 등록
	@PostMapping("/post/comment/add")
	public String addComment(@RequestParam String content) {
		if (content != null && !content.trim().isEmpty()) {
			Comment newComment = new Comment(commentIdCounter++, content, "익명");
			newComment.setCreatedAt(LocalDateTime.now());
			post.addComment(newComment);
		}
		return "redirect:/post";
	}
	
	//댓글 삭제
	@PostMapping("/post/comment/delete")
	public String deleteComment(@RequestParam Long commentId) {
		post.removeComment(commentId);
		return "redirect:/post";
	}
	
	//댓글 수정
	@PostMapping("/post/comment/edit")
	public String editComment(
			@RequestParam Long commentId,
			@RequestParam String content) {
		Comment comment = post.findCommentById(commentId);
		if (comment != null && content != null && !content.trim().isEmpty()) {
			comment.setContent(content);
		}
		return "redirect:/post";
	}
	
	//댓글 좋아요
	@PostMapping("/post/comment/like")
	public String toggleCommentLike(@RequestParam Long commentId) {
		Comment comment = post.findCommentById(commentId);
		if (comment != null) {
			if (comment.isLikedByMe()) {
				comment.setLikeCount(comment.getLikeCount() - 1);
				comment.setLikedByMe(false);
			} else {
				comment.setLikeCount(comment.getLikeCount() + 1);
				comment.setLikedByMe(true);
			}
		}
		return "redirect:/post";
	}
	
	//대댓글 추가
	@PostMapping("/post/comment/reply")
	public String addReply(
			@RequestParam Long parentId,
			@RequestParam String content) {
		Comment parent = post.findCommentById(parentId);
		if (parent != null && content != null && !content.trim().isEmpty()) {
			Comment reply = new Comment(commentIdCounter++, content, "익명");
			reply.setCreatedAt(LocalDateTime.now());
			parent.getReplies().add(reply);
		}
		return "redirect:/post";
	}
	
	// 대댓글 좋아요
	@PostMapping("/post/comment/reply/like")
	public String toggleReplyLike(
	        @RequestParam Long parentId,
	        @RequestParam Long replyId) {
	    
	    Comment parent = post.findCommentById(parentId);
	    if (parent != null) {
	        Comment reply = parent.getReplies().stream()
	            .filter(r -> r.getId().equals(replyId))
	            .findFirst()
	            .orElse(null);
	        
	        if (reply != null) {
	            if (reply.isLikedByMe()) {
	                reply.setLikeCount(reply.getLikeCount() - 1);
	                reply.setLikedByMe(false);
	            } else {
	                reply.setLikeCount(reply.getLikeCount() + 1);
	                reply.setLikedByMe(true);
	            }
	        }
	    }
	    
	    return "redirect:/post";
	}
	
	// 대댓글 수정
	@PostMapping("/post/comment/reply/edit")
	public String editReply(
	        @RequestParam Long parentId,
	        @RequestParam Long replyId,
	        @RequestParam String content) {
	    
	    Comment parent = post.findCommentById(parentId);
	    if (parent != null) {
	        Comment reply = parent.getReplies().stream()
	            .filter(r -> r.getId().equals(replyId))
	            .findFirst()
	            .orElse(null);
	        
	        if (reply != null && content != null && !content.trim().isEmpty()) {
	            reply.setContent(content);
	        }
	    }
	    
	    return "redirect:/post";
	}
	
	// 대댓글 삭제
	@PostMapping("/post/comment/reply/delete")
	public String deleteReply(
	        @RequestParam Long parentId,
	        @RequestParam Long replyId) {
	    
	    Comment parent = post.findCommentById(parentId);
	    if (parent != null) {
	        parent.getReplies().removeIf(r -> r.getId().equals(replyId));
	    }
	    
	    return "redirect:/post";
	}
}