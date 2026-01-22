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
}