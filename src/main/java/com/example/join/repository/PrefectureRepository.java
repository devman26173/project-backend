package com.example.join.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.join.entity.Prefecture;

public interface PrefectureRepository extends JpaRepository<Prefecture, String> {
	
}
