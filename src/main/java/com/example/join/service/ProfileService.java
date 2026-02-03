package com.example.join.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.join.entity.Profile;
import com.example.join.repository.ProfileRepository;
import com.example.join.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProfileService {

	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;
	
	public ProfileService(ProfileRepository profileRepository,
			UserRepository userRepository) {
		this.profileRepository = profileRepository;
		this.userRepository = userRepository;
	}

	//userIdからProfileを取得（同時リクエスト対応）
	@Transactional
	public Profile getByUserId(Long userId) {
	    return profileRepository.findByUser_UserId(userId)
	        .orElseGet(() -> createProfileIfNotExists(userId));
	}

	//Profileが存在しない場合に作成（unique制約違反時は再検索）
	@Transactional
	private Profile createProfileIfNotExists(Long userId) {
	    try {
	        Profile p = new Profile();
	        p.setUser(userRepository.findById(userId)
	                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId)));
	        p.setIntroduction("");
	        p.setImageUrl(null);
	        return profileRepository.save(p);
	    } catch (DataIntegrityViolationException e) {
	        // 同時リクエストでunique制約違反が発生した場合、再度検索
	        log.warn("Unique constraint violation for userId: {}. Retrying query.", userId);
	        return profileRepository.findByUser_UserId(userId)
	            .orElseThrow(() -> new IllegalStateException(
	                "Failed to retrieve profile after unique constraint violation for userId: " + userId, e));
	    }
	}


	
	//更新処理（Profileが存在しない場合は作成してから更新）
	public void updateProfile (Long userId, Profile formProfile) {
		Profile profile = getOrCreateProfile(userId);
		
		profile.setIntroduction(formProfile.getIntroduction());
		profile.setImagePath(formProfile.getImagePath());
		
		profileRepository.save(profile);
	}

}
