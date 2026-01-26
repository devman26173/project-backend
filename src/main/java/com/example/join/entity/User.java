package com.example.join.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
 // ✅ 추가 권장
    private String username;
    
    private String password;
    private String region;
    
 // ✅ 추가: 양방향 관계 (선택사항)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    public Long getId() {
		  return id;
    }
    public void setId(Long id) {
	    this.id = id;
    }
 // ✅ 추가
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
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
 // ✅ 추가
    public List<Post> getPosts() {
        return posts;
    }
    
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}