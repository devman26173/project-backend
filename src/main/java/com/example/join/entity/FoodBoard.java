package com.example.join.entity;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class FoodBoard {
// public class Book
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    
    private String title; 
    private String region; 
    private String prefecture; 
    private Integer rating; 
    
@Column(columnDefinition = "TEXT")
	private String content; 
	private String imageUrls; 
	private LocalDateTime createdAt; 
	
@ManyToOne
@JoinColumn(name = "user_id")
   	private User user;

//✅ 추가: 좋아요 관련 필드 (DB 컬럼 아님, 조회시에만 사용)
@Transient
private Integer likeCount = 0;

@Transient
private Boolean likedByMe = false;
	
@PrePersist
	public void prePersist() {
	this.createdAt = LocalDateTime.now(); 
}


//Getter & Setter
  
public Long getId() {
	return Id;
}

public void setId(Long id) {
	Id = id;
}

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
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

public Integer getRating() {
	return rating;
}

public void setRating(Integer rating) {
	this.rating = rating;
}

public String getContent() {
	return content;
}

public void setContent(String content) {
	this.content = content;
}

public String getImageUrls() {
	return imageUrls;
}

public void setImageUrls(String imageUrls) {
	this.imageUrls = imageUrls;
}

public LocalDateTime getCreatedAt() {
	return createdAt;
}

public void setCreatedAt(LocalDateTime createdAt) {
	this.createdAt = createdAt;
}

public User getUser() {
    return user;
}

public void setUser(User user) {
    this.user = user;
}
//✅ 추가: likeCount getter/setter
public Integer getLikeCount() {
    return likeCount;
}
public void setLikeCount(Integer likeCount) {
    this.likeCount = likeCount;
}

// ✅ 추가: likedByMe getter/setter
public Boolean getLikedByMe() {
    return likedByMe;
}
public void setLikedByMe(Boolean likedByMe) {
    this.likedByMe = likedByMe;
}

}