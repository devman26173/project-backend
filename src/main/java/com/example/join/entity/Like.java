package com.example.join.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long targetId;      // 게시글/댓글/대댓글 ID
    private String targetType;  // "POST", "COMMENT", "REPLY"
    private String userId;      // 현재는 "user1" 같은 임시값
    
    public Like() {}
    
    public Like(Long targetId, String targetType, String userId) {
        this.targetId = targetId;
        this.targetType = targetType;
        this.userId = userId;
    }
    
    // Getter / Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
    
    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}