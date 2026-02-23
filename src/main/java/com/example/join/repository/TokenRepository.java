package com.example.join.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.join.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

}

