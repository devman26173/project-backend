package com.example.join.dto;

import com.example.join.entity.Comment;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentDTO {
    
    private Long id;
    private String username;
    private String content;
    private String time;
    private List<CommentDTO> replies = new ArrayList<>();
    
    public static CommentDTO fromEntity(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setUsername(comment.getUsername());
        dto.setContent(comment.getContent());
        dto.setTime(formatTime(comment.getTime()));
        
        if (comment.getReplies() != null) {
            dto.setReplies(
                comment.getReplies().stream()
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
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public List<CommentDTO> getReplies() {
        return replies;
    }
    
    public void setReplies(List<CommentDTO> replies) {
        this.replies = replies;
    }
}