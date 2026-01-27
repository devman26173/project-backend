package com.example.join.service;

import com.example.join.entity.User;
import com.example.join.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
// public class Bookservice
		private final UserRepository userRepository;
		// private final BookRepository bookRepository;

    public UserService(UserRepository userRepository) {
    // public BookService(BookRepository bookRepository)
        this.userRepository = userRepository;
    }
    public List<User> findAll() {
        return userRepository.findAll();
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
    	// 1. Repository를 통해 아이디로 사용자 찾기
        // (UserRepository에 findByUsername 메소드 선언이 필요)
    	return userRepository.findByUsername(username)
    			.filter(u -> u.getPassword().equals(password))//비밀번호대조
    			.orElse(null); //틀리면 null반환
    }
}