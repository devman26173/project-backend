package com.example.join.service;

import org.springframework.stereotype.Service;

import com.example.join.entity.Profile;
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

	//userIdからProfileを取得
	public Profile getByUserId(Long userId) {
	    return profileRepository.findByUser_UserId(userId)
	        .orElseGet(() -> {
	            Profile p = new Profile();
	            p.setUser(userRepository.findById(userId)
	                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId)));
	            p.setIntroduction("");
	            p.setImagePath(null);
	            return profileRepository.save(p);
	        });
	}


	
	//更新処理
	public void updateProfile (Long userId, Profile formProfile) {
		Profile profile = getByUserId(userId);
		
		profile.setIntroduction(formProfile.getIntroduction());
		profile.setImagePath(formProfile.getImagePath());
		
		profileRepository.save(profile);
	}

}
