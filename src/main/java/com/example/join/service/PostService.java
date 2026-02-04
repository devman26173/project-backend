package com.example.join.service;

import com.example.join.entity.Post;
import com.example.join.entity.Like;
import com.example.join.repository.PostRepository;
import com.example.join.repository.LikeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    
    public PostService(PostRepository postRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
    }
    
    public List<Post> findAll() {
        return postRepository.findAll();
    }
    
    // ========== 좋아요 관련 메서드 추가 ==========
    
    // 좋아요 토글 (있으면 삭제, 없으면 추가)
    public void toggleLike(Long targetId, String targetType, String userId) {
        Optional<Like> existingLike = likeRepository.findByTargetIdAndTargetTypeAndUserId(
            targetId, targetType, userId
        );
        
        if (existingLike.isPresent()) {
            // 이미 좋아요 했으면 삭제
            likeRepository.delete(existingLike.get());
        } else {
            // 좋아요 안 했으면 추가
            Like newLike = new Like(targetId, targetType, userId);
            likeRepository.save(newLike);
        }
    }
    
    // 좋아요 개수 조회
    public int getLikeCount(Long targetId, String targetType) {
        return (int) likeRepository.countByTargetIdAndTargetType(targetId, targetType);
    }
    
    // 좋아요 여부 확인
    public boolean isLiked(Long targetId, String targetType, String userId) {
        return likeRepository.findByTargetIdAndTargetTypeAndUserId(
            targetId, targetType, userId
        ).isPresent();
    }
}