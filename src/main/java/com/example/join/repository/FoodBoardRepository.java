package com.example.join.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.join.entity.FoodBoard;


public interface FoodBoardRepository extends JpaRepository<FoodBoard, Long> {

}
