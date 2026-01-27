package com.example.join.repository;

import com.example.join.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 게시글의 댓글들 (대댓글 제외)
    List<Comment> findByPostIdAndParentIdIsNull(Long postId);
    
    // 특정 댓글의 대댓글들
    List<Comment> findByParentId(Long parentId);
    
    // 특정 게시글의 모든 댓글 (대댓글 포함)
    List<Comment> findByPostId(Long postId);
}