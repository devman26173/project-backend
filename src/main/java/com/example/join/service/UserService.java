package com.example.join.service;

import com.example.join.entity.User;
import com.example.join.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
    
   
    
    public void registerUser(String username, String name, String password, String region, String prefecture) {
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setPassword(password);
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

            if(user.getPassword().equals(password)) {
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
    
    // ✅ 이 메서드 추가!
    public void logout(HttpSession session) {
        session.invalidate();
    }
}