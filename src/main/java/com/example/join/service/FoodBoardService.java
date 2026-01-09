package com.example.join.service;

import com.example.join.entity.FoodBoard;

import com.example.join.repository.FoodBoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodBoardService {

	private final FoodBoardRepository foodboardRepository;
		
    public FoodBoardService(FoodBoardRepository foodboardRepository) {
        this.foodboardRepository = foodboardRepository;
    }
    public List<FoodBoard> findAll() {
        return foodboardRepository.findAll();
    }
}