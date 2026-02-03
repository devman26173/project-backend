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

	//userIdからProfileを取得（読み取り専用 - DBへの書き込みなし）
	public Profile getByUserId(Long userId) {
	    return profileRepository.findByUser_UserId(userId)
	        .orElseGet(() -> {
	            // DB保存なしで基本プロフィールオブジェクトを返す
	            Profile p = new Profile();
	            p.setUser(userRepository.findById(userId).orElseThrow());
	            p.setIntroduction("");
	            p.setImageUrl(null);
	            return p;
	        });
	}
	
	//userIdからProfileを取得または作成（書き込み操作）
	public Profile getOrCreateProfile(Long userId) {
	    return profileRepository.findByUser_UserId(userId)
	        .orElseGet(() -> {
	            Profile p = new Profile();
	            p.setUser(userRepository.findById(userId).orElseThrow());
	            p.setIntroduction("");
	            p.setImageUrl(null);
	            return profileRepository.save(p);
	        });
	}


	
	//更新処理（Profileが存在しない場合は作成してから更新）
	public void updateProfile (Long userId, Profile formProfile) {
		Profile profile = getOrCreateProfile(userId);
		
		profile.setIntroduction(formProfile.getIntroduction());
		profile.setImageUrl(formProfile.getImageUrl());
		
		profileRepository.save(profile);
	}

}
