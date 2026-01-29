package com.example.join.service;

import org.springframework.stereotype.Service;

import com.example.join.entity.Profile;
import com.example.join.repository.ProfileRepository;

@Service
public class ProfileService {

	private final ProfileRepository repository;

	public ProfileService(ProfileRepository repository) {
		this.repository = repository;
	}

	public Profile getProfile(Long id) {
		return repository.findById(id).orElseThrow();
	}

	public void update(Profile form) {
		Profile p = repository.findById(form.getProfileId())
				.orElseThrow(() -> new IllegalArgumentException("profile not founded"));
		p.setIntroduction(form.getIntroduction());
		//p.setImagePath(form.getImagePath());
		repository.save(p);
	}

}
