package com.example.join.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestErrorController {

    @GetMapping("/test/500")
    public String trigger500() {
        throw new RuntimeException("forced 500 for browser test");
    }
}
