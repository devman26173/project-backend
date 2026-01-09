package com.example.join.service;

import com.example.join.dto.PostDTO;
import com.example.join.entity.Post;
import com.example.join.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PostService {
    
    private final PostRepository postRepository;
    
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    
    public List<PostDTO> getAllPosts() {
        return postRepository.findAllByOrderByTimeDesc().stream()
            .map(PostDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findByIdWithComments(id)
            .orElseThrow(() -> new RuntimeException("投稿が見つかりません: " + id));
        return PostDTO.fromEntity(post);
    }
}