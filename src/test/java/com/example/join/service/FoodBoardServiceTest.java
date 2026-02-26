package com.example.join.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.join.entity.FoodBoard;
import com.example.join.repository.FoodBoardRepository;

@ExtendWith(MockitoExtension.class)
public class FoodBoardServiceTest {
	
	@Mock
	FoodBoardRepository foodboardRepository;
	
	@InjectMocks
	FoodBoardService foodBoardService; 
	
	@Test
	void 전체_게시글_조회() {
		FoodBoard board = new FoodBoard(); 
		board.setTitle("라멘집");
		
		when(foodboardRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(board));
		
		List<FoodBoard> result = foodBoardService.findAll();
		
		assertEquals(1, result.size());
		verify(foodboardRepository, times(1)).findAllByOrderByCreatedAtDesc();
	}
	
	@Test
	void 게시글_저장() {
		FoodBoard board = new FoodBoard(); 
		board.setTitle("스시집");
		
		foodboard
	}
	

}
