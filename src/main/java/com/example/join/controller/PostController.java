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
import com.example.join.service.PostService;  

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostController {
    
    private final PostService postService;  
    private final CommentService commentService;
    
    private Post post = new Post();
    
    public PostController(PostService postService, CommentService commentService) {  
        this.postService = postService;  
        this.commentService = commentService;
        
        // 임시 User 생성
        User tempUser = new User();
        tempUser.setUserId(1L);
        tempUser.setUsername("1234");
        
        // Post 설정
        post.setId(1L);
        post.setContent("こんにちは❣");
        post.setUser(tempUser);
        
    }

}