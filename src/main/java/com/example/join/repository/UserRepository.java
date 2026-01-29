package com.example.join.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.join.entity.User;
// import com.example.join.entity.Book;

public interface UserRepository extends JpaRepository<User, Long> {
// public interface Bookrepository extends JpaRepository {
}