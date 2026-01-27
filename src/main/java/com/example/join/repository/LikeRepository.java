package com.example.join.repository;

import com.example.join.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    // 특정 대상에 대한 특정 유저의 좋아요 찾기
    Optional<Like> findByTargetIdAndTargetTypeAndUserId(Long targetId, String targetType, String userId);
    
    // 특정 대상의 좋아요 개수 세기
    long countByTargetIdAndTargetType(Long targetId, String targetType);
    
    // 특정 대상의 모든 좋아요 가져오기
    List<Like> findByTargetIdAndTargetType(Long targetId, String targetType);
}