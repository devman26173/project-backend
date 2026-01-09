package com.example.join.controller;

import com.example.join.entity.FoodBoard;
import com.example.join.service.FoodBoardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/FoodBoards")
public class FoodBoardController {
    private final FoodBoardService foodboardService;
    // private final BoookService bookService;

    public FoodBoardController(FoodBoardService foodboardService) {
    /// public bookController(BookService bookService)
		this.foodboardService = foodboardService;
    }
    
    @GetMapping
    public List<FoodBoard> findAll() {
    // public List<Book> findAll()
        return foodboardService.findAll();
    }
}
