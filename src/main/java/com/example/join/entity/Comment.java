package com.example.join.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long postId;        // 어느 게시글의 댓글인지
    private Long parentId;      // 대댓글인 경우 부모 댓글 ID (null이면 댓글)
    
    private String content;
    private String author;	// 임시로 유지
    private LocalDateTime createdAt;
    
    // ✅ 추가: User와 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Transient
    private int likeCount;
    
    @Transient
    private boolean likedByMe;
    
    @Transient
    private List<Comment> replies = new ArrayList<>();
    
    public Comment() {}
    
    public Comment(Long id, Long postId, String content, String author) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.author = author;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getter / Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    // ✅ 추가: User getter/setter
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
    
    public boolean isLikedByMe() { return likedByMe; }
    public void setLikedByMe(boolean likedByMe) { this.likedByMe = likedByMe; }
    
    public List<Comment> getReplies() { return replies; }
    public void setReplies(List<Comment> replies) { this.replies = replies; }

}