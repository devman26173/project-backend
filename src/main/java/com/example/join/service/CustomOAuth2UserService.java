package com.example.join.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.join.entity.CustomOAuth2User;
import com.example.join.entity.User;
import com.example.join.repository.UserRepository;

@Service
public class CustomOAuth2UserService
        extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(request);

        String provider =
                request.getClientRegistration()
                       .getRegistrationId();

        String providerId =
                oAuth2User.getAttribute("sub"); // google 기준

        String email =
                oAuth2User.getAttribute("email");

        // 회원 조회
        User user = userRepository
        	    .findByProviderAndProviderId(provider, providerId)
        	    .orElseGet(() -> userRepository.save(
        	        User.builder()
        	            .email(email)
        	            .role("ROLE_USER")
        	            .provider(provider)
        	            .providerId(providerId)
        	            .build()
        	    ));

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }
}
