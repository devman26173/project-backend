package com.example.join.dto;

import com.example.join.entity.Post;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostDTO {
    
    private Long id;
    private String username;
    private String title;
    private String content;
    private Integer likes;
    private String time;
    private List<CommentDTO> comments = new ArrayList<>();
    
    public static PostDTO fromEntity(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setUsername(post.getUsername());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setLikes(post.getLikes());
        dto.setTime(formatTime(post.getTime()));
        
        if (post.getComments() != null) {
            dto.setComments(
                post.getComments().stream()
                    .filter(comment -> comment.getParentComment() == null)
                    .map(CommentDTO::fromEntity)
                    .collect(Collectors.toList())
            );
        }
        return dto;
    }
    
    private static String formatTime(LocalDateTime time) {
        if (time == null) return "";
        
        LocalDateTime now = LocalDateTime.now();
        long minutes = Duration.between(time, now).toMinutes();
        
        if (minutes < 1) return "たった今";
        if (minutes < 60) return minutes + "分前";
        if (minutes < 1440) return (minutes / 60) + "時間前";
        if (minutes < 10080) return (minutes / 1440) + "日前";
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Integer getLikes() {
        return likes;
    }
    
    public void setLikes(Integer likes) {
        this.likes = likes;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public List<CommentDTO> getComments() {
        return comments;
    }
    
    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }
}