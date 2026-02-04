package com.example.join.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.join.entity.Post;
import com.example.join.entity.Comment;
import com.example.join.entity.User; 
import com.example.join.service.CommentService;
import com.example.join.service.PostService;  // ✅ 변경

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostController {
    
    private final PostService postService;  // ✅ 변경
    private final CommentService commentService;
    
    private Post post = new Post();
    
    public PostController(PostService postService, CommentService commentService) {  // ✅ 변경
        this.postService = postService;  // ✅ 변경
        this.commentService = commentService;
        
        // 임시 User 생성
        User tempUser = new User();
        tempUser.setUserId(1L);
        tempUser.setUsername("1234");
        
        // Post 설정
        post.setId(1L);
        post.setContent("こんにちは❣");
        post.setUser(tempUser);
        
        Comment sampleComment = new Comment(null, 1L, "今日。。。寒いですね！", "ユーザー1");
        sampleComment.setCreatedAt(LocalDateTime.now().minusMinutes(30));
        commentService.save(sampleComment);
    }

    @GetMapping("/post")
    public String post(Model model, HttpSession session) {
        String currentUser = "user1";
        
        User loginUser = (User) session.getAttribute("loginUser");
        boolean isLoggedIn = (loginUser != null);
        
        // ✅ PostService 사용
        post.setLikeCount(postService.getLikeCount(post.getId(), "POST"));
        post.setLikedByMe(postService.isLiked(post.getId(), "POST", currentUser));
        
        List<Comment> comments = commentService.findByPostId(post.getId());
        
        for (Comment comment : comments) {
            // ✅ PostService 사용
            comment.setLikeCount(postService.getLikeCount(comment.getId(), "COMMENT"));
            comment.setLikedByMe(postService.isLiked(comment.getId(), "COMMENT", currentUser));
            
            List<Comment> replies = commentService.findRepliesByParentId(comment.getId());
            for (Comment reply : replies) {
                // ✅ PostService 사용
                reply.setLikeCount(postService.getLikeCount(reply.getId(), "COMMENT"));
                reply.setLikedByMe(postService.isLiked(reply.getId(), "COMMENT", currentUser));
            }
            comment.setReplies(replies);
        }
        
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("commentCount", comments.size());
        model.addAttribute("isLoggedIn", isLoggedIn);
        
        return "post";
    }
    
    @PostMapping("/post/like")
    public String toggleLike() {
        String currentUser = "user1";
        // ✅ PostService 사용
        postService.toggleLike(post.getId(), "POST", currentUser);
        
        return "redirect:/post";
    }
    
    @PostMapping("/post/comment/add")
    public String addComment(@RequestParam String content, HttpSession session) {
        if (content != null && !content.trim().isEmpty()) {
            User loginUser = (User) session.getAttribute("loginUser");
            
            Comment newComment = new Comment(null, post.getId(), content, "ユーザー");
            newComment.setCreatedAt(LocalDateTime.now());
            
            if (loginUser != null) {
                newComment.setUser(loginUser);
                newComment.setAuthor(loginUser.getName());
            }
            
            commentService.save(newComment);
        }
        return "redirect:/post";
    }
    
    @PostMapping("/post/comment/delete")
    public String deleteComment(@RequestParam Long commentId) {
        Comment comment = commentService.findById(commentId);
        if (comment != null) {
            // 대댓글들도 삭제
            List<Comment> replies = commentService.findRepliesByParentId(commentId);
            for (Comment reply : replies) {
                commentService.delete(reply);
            }
            
            // 댓글 삭제
            commentService.delete(comment);
        }
        
        return "redirect:/post";
    }
    
    @PostMapping("/post/comment/edit")
    public String editComment(
            @RequestParam Long commentId,
            @RequestParam String content) {
        Comment comment = commentService.findById(commentId);
        if (comment != null && content != null && !content.trim().isEmpty()) {
            comment.setContent(content);
            commentService.save(comment);
        }
        return "redirect:/post";
    }
    
    @PostMapping("/post/comment/like")
    public String toggleCommentLike(@RequestParam Long commentId) {
        String currentUser = "user1";
        // ✅ PostService 사용
        postService.toggleLike(commentId, "COMMENT", currentUser);
        
        return "redirect:/post";
    }
    
    @PostMapping("/post/comment/reply")
    public String addReply(
            @RequestParam Long parentId,
            @RequestParam String content,
            HttpSession session) {
        if (content != null && !content.trim().isEmpty()) {
            User loginUser = (User) session.getAttribute("loginUser");
            
            Comment reply = new Comment(null, post.getId(), content, "ユーザー");
            reply.setParentId(parentId);
            reply.setCreatedAt(LocalDateTime.now());
            
            if (loginUser != null) {
                reply.setUser(loginUser);
                reply.setAuthor(loginUser.getName());
            }
            
            commentService.save(reply);
        }
        return "redirect:/post";
    }
    
    @PostMapping("/post/comment/reply/like")
    public String toggleReplyLike(
            @RequestParam Long parentId,
            @RequestParam Long replyId) {
        
        String currentUser = "user1";
        // ✅ PostService 사용
        postService.toggleLike(replyId, "COMMENT", currentUser);
        
        return "redirect:/post";
    }
    
    @PostMapping("/post/comment/reply/edit")
    public String editReply(
            @RequestParam Long parentId,
            @RequestParam Long replyId,
            @RequestParam String content) {
        
        Comment reply = commentService.findById(replyId);
        if (reply != null && content != null && !content.trim().isEmpty()) {
            reply.setContent(content);
            commentService.save(reply);
        }
        
        return "redirect:/post";
    }
    
    @PostMapping("/post/comment/reply/delete")
    public String deleteReply(
            @RequestParam Long parentId,
            @RequestParam Long replyId) {
        
        Comment reply = commentService.findById(replyId);
        if (reply != null) {
            commentService.delete(reply);
        }
        
        return "redirect:/post";
    }
}