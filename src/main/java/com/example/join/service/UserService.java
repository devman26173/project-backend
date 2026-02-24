package com.example.join.service;

import com.example.join.entity.User;
import com.example.join.repository.UserRepository;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    //암호와 도구(SecurityConfig에서 등록한 BCryptPasswordEncoder가 들어옴)
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, 
    				PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public void registerUser(String username, String name, String password, String region, String prefecture) {
    	//입력한 username이 이미 db에 있는지 확인
    	Optional<User> existingUser = userRepository.findByUsername(username);
    	//중복된 ID면 예외 발생
    	if (existingUser.isPresent()) {
    		throw new IllegalArgumentException("このIDは既に使用されています。");
    	}
    	//중복 아니면 회원가입 진행
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setRegion(region);
        user.setPrefecture(prefecture);
        userRepository.save(user);
    }
    
    public User login(String username, String password) {
        System.out.println("=== UserService.login ===");
        System.out.println("찾는 username: " + username);
        System.out.println("입력한 password: " + password);

        Optional<User> userOpt = userRepository.findByUsername(username);

        if(userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("DB에서 찾은 user: " + user.getUsername());
            System.out.println("DB의 password: " + user.getPassword());

            if(passwordEncoder.matches(password, user.getPassword())) {
                System.out.println("✅ 비밀번호 일치!");
                return user;
            } else {
                System.out.println("❌ 비밀번호 불일치!");
                return null;
            }
        } else {
            System.out.println("❌ 사용자를 찾을 수 없음!");
            return null;
        }
    }
    
    // 
    public void logout(HttpSession session) {
        session.invalidate();
    }
    //db에서 사용자 삭제
    @Transactional
    public void withdrawUser(Long userId) {
    	userRepository.deleteById(userId);
    }
    //비밀번호 검증,matches()가 내부적으로 비교해서 참/거짓 리턴
    public boolean verifyPassword(User user, String rawPassword) {
    	return passwordEncoder.matches(rawPassword, user.getPassword());
    }
    //비밀번호 변경 메서드 (새 비밀번호를 암호화해서 db에 업데이트)
    //트랜잭션 = 작업도중 에러나면 변경사항을 자동으로 되돌리기
    @Transactional
    public void changePassword(Long userId, String newPassword) {
    	//userId로 db에서 사용자 찾기
    	//orElesThrow = 못찾으면 예외발생
    	User user = userRepository.findById(userId)
    		.orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません。"));
    	//새 비밀번호를 암호화해서 저장
    	user.setPassword(passwordEncoder.encode(newPassword));
    	//save = db에 업데이트
    	userRepository.save(user);
    }
}