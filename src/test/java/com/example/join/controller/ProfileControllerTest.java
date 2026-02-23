package com.example.join.controller;

import com.example.join.entity.Profile;
import com.example.join.entity.User;
import com.example.join.repository.ProfileRepository;
import com.example.join.repository.UserRepository;
import com.example.join.service.ProfileService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ProfileService.class) // DataJpaTestはServiceを自動で拾わないので明示
class ProfileServiceTest {

    @Autowired
    ProfileService profileService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Test
    void getByUserId_createsProfile_whenNotExists() {
        // ユーザーだけ先に作る
        User u = new User();
        u.setUsername("minchang");
        u.setPassword("1234");
        u.setName("Minchang");
        u = userRepository.save(u);

        // profileが無い状態で呼ぶと作られるはず
        Profile p = profileService.getByUserId(u.getUserId());

        assertThat(p).isNotNull();
        assertThat(p.getUser()).isNotNull();
        assertThat(p.getUser().getUserId()).isEqualTo(u.getUserId());

        // DBにも保存されてるか
        assertThat(profileRepository.findByUser_UserId(u.getUserId())).isPresent();
    }

    @Test
    void getByUserId_returnsExistingProfile_whenExists() {
        User u = new User();
        u.setUsername("minchang");
        u.setPassword("1234");
        u.setName("Minchang");
        u = userRepository.save(u);

        Profile saved = new Profile();
        saved.setUser(u);
        saved.setIntroduction("hello");
        saved.setImageUrl("https://example.com/p.png");
        profileRepository.save(saved);

        Profile p = profileService.getByUserId(u.getUserId());

        assertThat(p.getIntroduction()).isEqualTo("hello");
        assertThat(p.getImageUrl()).isEqualTo("https://example.com/p.png");
    }
}