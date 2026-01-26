package com.example.join.entity;

import jakarta.persistence.*;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String content;
    
 // ✅ 추가: User와의 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Transient (DB 저장 안 함)
    @Transient
    private int likeCount;
    
    @Transient
    private boolean likedByMe;
    
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
    
 // ✅ 추가: User getter/setter
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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