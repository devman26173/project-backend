package com.example.join.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "introduction", length = 1000)
    private String introduction;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

	// コンストラクタ
	protected Profile() {
	}

	public Profile(User user, String imagePath) {
		this.user = user;
		this.imagePath = imagePath;
	}

	// getterSetter
	public Long getProfileId() {
		return profileId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public User getUser() {
		return user;
	}

	public String getIntroduction() {
		return introduction;
	}
	
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
}
