package com.example.join.controller;

import com.example.join.entity.User;
// import com.example.join.entity.Book;
import com.example.join.service.UserService;
// import com.example.join.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService signupformService;
    // private final BoookService bookService;

    public UserController(UserService signupformService) {
    // public bookController(BookService bookService)
		this.signupformService = signupformService;
    }

    @GetMapping
    public List<User> findAll() {  // 괄호 2개로 수정
    // public List<Book> findAll()
        return signupformService.findAll();  // 세미콜론도 추가
    }//본인 파일명으로 수정
}