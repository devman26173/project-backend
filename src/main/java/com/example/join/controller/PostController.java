package com.example.join.controller;

import com.example.join.entity.Post;
// import com.example.join.entity.Book;
import com.example.join.service.PostService;
// import com.example.join.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")  
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    // private final BoookService bookService;

    public PostController(PostService postService) {
    // public bookController(BookService bookService)
		this.postService = postService;
    }

    @GetMapping
    public List<Post> findAll() { 
    // public List<Book> findAll()
        return postService.findAll();
    }
}