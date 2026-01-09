package com.example.join.repository;

import com.example.join.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.parentComment IS NULL ORDER BY c.time DESC")
    List<Comment> findTopLevelCommentsByPostId(Long postId);
    
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.replies WHERE c.id = :commentId")
    Comment findByIdWithReplies(Long commentId);
}