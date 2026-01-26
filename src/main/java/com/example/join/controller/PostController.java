package com.example.join.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.join.entity.Post;
import com.example.join.entity.Comment;
import com.example.join.entity.Like;
import com.example.join.entity.User;  // ✅ 추가
import com.example.join.repository.LikeRepository;
import com.example.join.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostController {
    
    private final LikeRepository likeRepository;
    private final CommentService commentService;
    
    private Post post = new Post();
    
    public PostController(LikeRepository likeRepository, CommentService commentService) {
        this.likeRepository = likeRepository;
        this.commentService = commentService;
        
     // ✅ 추가: 임시 User 생성
        User tempUser = new User();
        tempUser.setId(1L);
        tempUser.setUsername("1234");
        
        // ✅ Post 설정
        post.setId(1L);
        post.setContent("첫 게시글");
        post.setUser(tempUser);  // ✅ User 연결
        
        Comment sampleComment = new Comment(null, 1L, "첫 댓글입니다!", "유저1");
        sampleComment.setCreatedAt(LocalDateTime.now().minusMinutes(30));
        commentService.save(sampleComment);
    }

    @GetMapping("/post")
    public String post(Model model) {
        String currentUser = "user1";
        
        // 게시글 좋아요 정보
        post.setLikeCount((int) likeRepository.countByTargetIdAndTargetType(post.getId(), "POST"));
        post.setLikedByMe(likeRepository.findByTargetIdAndTargetTypeAndUserId(
            post.getId(), "POST", currentUser).isPresent());
        
        // DB에서 댓글 조회
        List<Comment> comments = commentService.findByPostId(post.getId());
        
        // 각 댓글의 좋아요 정보 및 대댓글 로드
        for (Comment comment : comments) {
            // 댓글 좋아요
            comment.setLikeCount((int) likeRepository.countByTargetIdAndTargetType(
                comment.getId(), "COMMENT"));
            comment.setLikedByMe(likeRepository.findByTargetIdAndTargetTypeAndUserId(
                comment.getId(), "COMMENT", currentUser).isPresent());
            
            // 대댓글 조회
            List<Comment> replies = commentService.findRepliesByParentId(comment.getId());
            for (Comment reply : replies) {
                reply.setLikeCount((int) likeRepository.countByTargetIdAndTargetType(
                    reply.getId(), "REPLY"));
                reply.setLikedByMe(likeRepository.findByTargetIdAndTargetTypeAndUserId(
                    reply.getId(), "REPLY", currentUser).isPresent());
            }
            comment.setReplies(replies);
        }
        
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("commentCount", comments.size());
        return "post";
    }
    
    @PostMapping("/post/like")
    public String toggleLike() {
        String currentUser = "user1";
        var existingLike = likeRepository.findByTargetIdAndTargetTypeAndUserId(
            post.getId(), "POST", currentUser);
        
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        } else {
            likeRepository.save(new Like(post.getId(), "POST", currentUser));
        }
        
        return "redirect:/post";
    }
    
    @PostMapping("/post/comment/add")
    public String addComment(@RequestParam String content) {
        if (content != null && !content.trim().isEmpty()) {
            // ✅ ID는 null로 설정 (JPA가 자동 생성)
            Comment newComment = new Comment(null, post.getId(), content, "익명");
            newComment.setCreatedAt(LocalDateTime.now());
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
                // 대댓글 좋아요 삭제
                likeRepository.findByTargetIdAndTargetType(reply.getId(), "REPLY")
                    .forEach(likeRepository::delete);
            }
            
            // 댓글 삭제
            commentService.delete(comment);
            
            // 댓글 좋아요 삭제
            likeRepository.findByTargetIdAndTargetType(commentId, "COMMENT")
                .forEach(likeRepository::delete);
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
        var existingLike = likeRepository.findByTargetIdAndTargetTypeAndUserId(
            commentId, "COMMENT", currentUser);
        
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        } else {
            likeRepository.save(new Like(commentId, "COMMENT", currentUser));
        }
        
        return "redirect:/post";
    }
    
    @PostMapping("/post/comment/reply")
    public String addReply(
            @RequestParam Long parentId,
            @RequestParam String content) {
        if (content != null && !content.trim().isEmpty()) {
            // ✅ ID는 null로 설정 (JPA가 자동 생성)
            Comment reply = new Comment(null, post.getId(), content, "익명");
            reply.setParentId(parentId);
            reply.setCreatedAt(LocalDateTime.now());
            commentService.save(reply);
        }
        return "redirect:/post";
    }
    
    @PostMapping("/post/comment/reply/like")
    public String toggleReplyLike(
            @RequestParam Long parentId,
            @RequestParam Long replyId) {
        
        String currentUser = "user1";
        var existingLike = likeRepository.findByTargetIdAndTargetTypeAndUserId(
            replyId, "REPLY", currentUser);
        
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        } else {
            likeRepository.save(new Like(replyId, "REPLY", currentUser));
        }
        
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
            
            // 대댓글 좋아요 삭제
            likeRepository.findByTargetIdAndTargetType(replyId, "REPLY")
                .forEach(likeRepository::delete);
        }
        
        return "redirect:/post";
    }
}