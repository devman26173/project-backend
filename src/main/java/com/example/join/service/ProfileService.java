package com.example.join.service;

import org.springframework.stereotype.Service;
import com.example.join.entity.Profile;
import com.example.join.entity.User;
import com.example.join.repository.ProfileRepository;
import com.example.join.repository.UserRepository; // 名前を変更したリポジトリ

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository; // 名前を統一

    // コンストラクタも UserRepository に合わせる
    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public Profile findByUserId(Long userId) {
        return profileRepository.findByUser_UserId(userId)
                .orElseGet(() -> {
                    // プロフィールがなければ、userRepositoryを使ってUserを探し、Profileを生成
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
                    
                    Profile newProfile = new Profile(user, "/images/default.png");
                    newProfile.setIntroduction("よろしくお願いします！");
                    return profileRepository.save(newProfile);
                });
    }

    public void update(Long userId, Profile form) {
        Profile p = profileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("profile not found"));
        
        p.setIntroduction(form.getIntroduction());
        profileRepository.save(p);
    }
}