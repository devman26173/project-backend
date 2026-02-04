package com.example.join.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.join.entity.Profile;
import com.example.join.entity.User;
import com.example.join.repository.ProfileRepository;
import com.example.join.repository.UserRepository;

@Service
public class ProfileService {

	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;
	
	public ProfileService(ProfileRepository profileRepository,
			UserRepository userRepository) {
		this.profileRepository = profileRepository;
		this.userRepository = userRepository;
	}

	// userIdからProfileを取得（存在しない場合はOptional.empty()を返す）
	public Optional<Profile> getByUserId(Long userId) {
	    return profileRepository.findByUser_UserId(userId);
	}

	// userIdからProfileを取得、存在しない場合は自動生成
	@Transactional
	public Profile getOrCreateProfile(Long userId) {
	    return profileRepository.findByUser_UserId(userId)
	        .orElseGet(() -> createDefaultProfile(userId));
	}

	// デフォルトプロフィールを作成
	private Profile createDefaultProfile(Long userId) {
	    User user = userRepository.findById(userId)
	        .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
	    
	    Profile profile = new Profile();
	    profile.setUser(user);
	    profile.setIntroduction("");
	    profile.setImageUrl(null);
	    
	    return profileRepository.save(profile);
	}

	// 更新処理
	@Transactional
	public void updateProfile(Long userId, Profile formProfile) {
		Profile profile = getOrCreateProfile(userId);
		
		profile.setIntroduction(formProfile.getIntroduction());
		profile.setImageUrl(formProfile.getImageUrl());
		
		profileRepository.save(profile);
	}

}
