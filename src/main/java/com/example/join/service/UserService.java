package com.example.join.service;

import com.example.join.entity.User;
// import com.example.join.entity.Book;
import com.example.join.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
// public class Bookservice
		private final UserRepository signupFormRepository;
		// private final BookRepository bookRepository;

    public UserService(UserRepository signupFormRepository) {
    // public BookService(BookRepository bookRepository)
        this.signupFormRepository = signupFormRepository;
    }
    public List<User> findAll() {
        return signupFormRepository.findAll();
    }
}