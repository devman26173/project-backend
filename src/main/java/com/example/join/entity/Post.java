package com.example.join.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String content;
    private int likeCount;
    private boolean likedByMe;
    
    // 댓글 리스트
    @Transient
    private List<Comment> comments = new ArrayList<>();
    
    // 댓글 내부 클래스
    public static class Comment {
    	private Long id;
    	private String content;
    	private String author;
    	
    	private int likeCount;
    	private boolean likedByMe;
    	
    	private List<Comment> replies = new ArrayList<>();
    	
    	public Comment(Long id, String content, String author) {
    		this.id = id;
    		this.content = content;
    		this.author = author;    		
    }
    	// getter / setter
    	public Long getId() { return id; }
    	public String getContent() { return content; }
    	public void setContent(String content) { this.content = content; }
    	
    	public int getLikeCount() { return likeCount; }
    	public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
    	
    	public boolean isLikedByMe() { return likedByMe; }
    	public void setLikedByMe(boolean likedByMe) {
    		this.likedByMe = likedByMe;
    	}
    	
    	public String getAuthor() { return author; }
    	public List<Comment> getReplies() { return replies; }
    }
    
    // Post getter / setter 
    public Long getId() {
		  return Id;
    }
    public void setId(Long Id) {
	    this.Id = Id;
    }
    public String getContent() {
	    return content;
    }
    public void setContent(String content){
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