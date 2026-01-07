package com.example.join.service;

import com.example.join.entity.Book;
import com.example.join.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    // CREATE
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    // READ (전체)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    // READ (단건)
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
    }

    // UPDATE
    public Book update(Long id, Book newBook) {
        Book book = findById(id);

        book.update(
                newBook.getTitle(),
                newBook.getAuthor(),
                newBook.getPrice()
            );
            return book;
    }

    // DELETE
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}