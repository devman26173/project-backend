package com.example.join.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.join.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

}
