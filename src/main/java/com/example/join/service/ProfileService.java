package com.example.join.service;

import org.springframework.stereotype.Service;
import com.example.join.entity.Profile;
import com.example.join.entity.User;
import com.example.join.repository.ProfileRepository;
import com.example.join.repository.UserRepository; // 追加
import jakarta.transaction.Transactional;

@Service
public class ProfileService {

    private final ProfileRepository repository;
    private final UserRepository userRepository; // 追加

    // コンストラクタに UserRepository を追加
    public ProfileService(ProfileRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Profile findByUserId(Long userId) {
        return repository.findByUser_UserId(userId)
                .orElseGet(() -> {
                    // プロフィールがない場合、自動で新規作成する
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
                    
                    Profile newProfile = new Profile(user, "/images/default-profile.png");
                    newProfile.setIntroduction("はじめまして！");
                    
                    // DBに保存して返す
                    return repository.save(newProfile);
                });
    }

    @Transactional
    public void update(Long userId, Profile updatedProfile) {
        Profile existingProfile = repository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        existingProfile.setIntroduction(updatedProfile.getIntroduction());
        existingProfile.setImagePath(updatedProfile.getImagePath());
        
        repository.save(existingProfile);
    }
}