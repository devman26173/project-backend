package com.example.join.controller;

import com.example.join.entity.SignupForm;
// import com.example.join.entity.Book;
import com.example.join.service.SignupFormService;
// import com.example.join.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/SignupForm")
public class SignupFormController {
    private final SignupFormService signupformService;
    // private final BoookService bookService;

    public SignupFormController(SignupFormService signupformService) {
    // public bookController(BookService bookService)
		this.signupformService = signupformService;
    }

    @GetMapping
    public List<SignupForm> findAll() {  // 괄호 2개로 수정
    // public List<Book> findAll()
        return signupformService.findAll();  // 세미콜론도 추가
    }//본인 파일명으로 수정
}