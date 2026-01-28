package com.example.join.entity;
import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name = "USERS")
@SequenceGenerator(
	name = "USER_SEQ_GEN",
	sequenceName = "USER_SEQ",
	allocationSize = 1
)

public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_GEN")
	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "USER_PW", nullable = false, length = 100)
	private String password;

	@Column(name = "USER_NAME", nullable = false, length = 50)
	private String userName;

	// ===== PROFILE (1:1, inverse) =====
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Profile profile;

    // ===== POST (1:N) =====
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Post> posts;
	
	
	//コンストラクタ
    protected User() {}

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
	
	//getterSetter
    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Profile getProfile() {
        return profile;
    }
	
	
}