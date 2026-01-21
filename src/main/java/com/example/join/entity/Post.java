package com.example.join.entity;

import jakarta.persistence.*;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String content;
    
    // ✅ 좋아요 정보만 Transient로 유지
    @Transient
    private int likeCount;
    
    @Transient
    private boolean likedByMe;
    
    // ❌ 삭제: Comment 내부 클래스 전체 삭제
    // ❌ 삭제: private List<Comment> comments
    // ❌ 삭제: addComment, removeComment, findCommentById 메서드
    
    // Getter / Setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public int getLikeCount() {
        return likeCount;
    }
    
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
    
    public boolean isLikedByMe() {
        return likedByMe;
    }
    
    public void setLikedByMe(boolean likedByMe) {
        this.likedByMe = likedByMe;
    }
}