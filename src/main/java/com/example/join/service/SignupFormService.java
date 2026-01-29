package com.example.join.service;

import com.example.join.entity.Profile;
import com.example.join.entity.User;
import com.example.join.repository.ProfileRepository;
import com.example.join.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SignupFormService {

    // 変数名は signupFormRepository (小文字) にします
    private final UserRepository signupFormRepository;
    private final ProfileRepository profileRepository;

    public SignupFormService(UserRepository signupFormRepository, ProfileRepository profileRepository) {
        this.signupFormRepository = signupFormRepository;
        this.profileRepository = profileRepository;
    }

    @Transactional
    public void save(User user) {
        // 1. Userエンティティを保存
        User savedUser = signupFormRepository.save(user);
        
        // 2. 保存したUserを紐付けてProfileを作成
        Profile profile = new Profile(savedUser, "/images/default.png");
        profile.setIntroduction("よろしくお願いします！");
        
        // 3. Profileを保存
        profileRepository.save(profile);
    }

    public List<User> findAll() {
        // ここも変数名(signupFormRepository)に変更
        return signupFormRepository.findAll();
    }
}