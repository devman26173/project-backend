package com.example.join.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.join.entity.Post;
// import com.example.join.entity.Book;

public interface PostRepository extends JpaRepository<Post, Long> {
// public interface Bookrepository extends JpaRepository {
}