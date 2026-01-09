package com.example.join.service;

import com.example.join.dto.CommentDTO;
import com.example.join.entity.Comment;
import com.example.join.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CommentService {
    
    private final CommentRepository commentRepository;
    
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        return commentRepository.findTopLevelCommentsByPostId(postId).stream()
            .map(CommentDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    public CommentDTO getCommentById(Long id) {
        Comment comment = commentRepository.findByIdWithReplies(id);
        if (comment == null) {
            throw new RuntimeException("コメントが見つかりません: " + id);
        }
        return CommentDTO.fromEntity(comment);
    }
}