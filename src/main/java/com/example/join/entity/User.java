package com.example.join.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "user_pw", nullable = false)
	private String password;

	@Column(name = "user_name", nullable = false)
	private String userName;

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	private Profile profile;

	// コンストラクタ
	public User() {
	}

	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	// getterSetter
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	

	public Profile getProfile() {
		return profile;
	}

}