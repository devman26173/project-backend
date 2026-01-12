package com.example.join.service;

import com.example.join.entity.SignupForm;
// import com.example.join.entity.Book;
import com.example.join.repository.SignupFormRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignupFormService {
// public class Bookservice
		private final SignupFormRepository signupFormRepository;
		// private final BookRepository bookRepository;

    public SignupFormService(SignupFormRepository signupFormRepository) {
    // public BookService(BookRepository bookRepository)
        this.signupFormRepository = signupFormRepository;
    }
    public List<SignupForm> findAll() {
        return signupFormRepository.findAll();
    }
}