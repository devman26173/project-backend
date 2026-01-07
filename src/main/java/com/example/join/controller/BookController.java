package com.example.join.controller;

import com.example.join.entity.Book;
import com.example.join.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	// CREATE
    @PostMapping
    public Book create(@RequestBody Book book) {
        return bookService.create(book);
    }

    // READ ALL
    @GetMapping
    public List<Book> findAll() {
        return bookService.findAll();
    }

    // READ ONE
    @GetMapping("/{id}")
    public Book findOne(@PathVariable Long id) {
        return bookService.findById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @RequestBody Book book) {
        return bookService.update(id, book);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }
}
