package com.example.join.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    private String username;
    private String name;
    private String password;
    private String region;
    private String prefecture;
    
    // ✅ 추가: 프로필 이미지 URL
    private String profileImageUrl;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    // Getter / Setter
    public Long getUserId() {
		  return userId;
    }
    public void setUserId(Long userId) {
	    this.userId = userId;
    }
    public String getUsername() {
    	return username;
    }
    public void setUsername(String username) {
    	this.username = username;
    }
    public String getName() {
    	return name;
    }
    public void setName(String name) {
    	this.name = name;
    }
    public String getPassword() {
	    return password;
    }
    public void setPassword(String password){
	    this.password = password;
    }
    public String getRegion() {
    	return region;
    }
    public void setRegion(String region) {
    	this.region = region;
    }
    public String getPrefecture() {
    	return prefecture;
    }
    public void setPrefecture(String prefecture) {
    	this.prefecture = prefecture;
    }
    
    // ✅ 추가: profileImageUrl getter/setter
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    public List<Post> getPosts() {
        return posts;
    }
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}