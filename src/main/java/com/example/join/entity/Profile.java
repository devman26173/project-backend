package com.example.join.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "PROFILE", uniqueConstraints = { @UniqueConstraint(columnNames = "USER_ID") }

)
@SequenceGenerator(name = "PROFILE_SEQ_GEN", sequenceName = "PROFILE_SEQ", allocationSize = 1)

public class Profile {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROFILE_SEQ_GEN")
	@Column(name = "PROFILE_ID")
	private Long profileId;

	@Column(name = "IMAGE_PATH", length = 255)
	private String imagePath;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false, unique = true

	)
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

}
