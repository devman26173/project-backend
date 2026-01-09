package com.example.join.service;

import com.example.join.entity.Post;
// import com.example.join.entity.Book;
import com.example.join.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
// public class Bookservice
		private final PostRepository postRepository;
		// private final BookRepository bookRepository;
    
    public PostService(PostRepository postRepository) {
    // public BookService(BookRepository bookRepository)
        this.postRepository = postRepository;
    }
    public List<Post> findAll() {
        return postRepository.findAll();
    }
}