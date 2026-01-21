package com.example.join.service;

import com.example.join.entity.Comment;
import com.example.join.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    
    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostIdAndParentIdIsNull(postId);
    }
    
    public List<Comment> findRepliesByParentId(Long parentId) {
        return commentRepository.findByParentId(parentId);
    }
    
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
    
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }
    
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }
}