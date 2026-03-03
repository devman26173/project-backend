package com.example.join.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

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
		
		when(foodboardRepository.findAllByOrderByCreatedAtDesc())
			.thenReturn(List.of(board));
		
		List<FoodBoard> result = foodBoardService.findAll(); 
		
		assertEquals(1, result.size());
		verify(foodboardRepository, times(1)).findAllByOrderByCreatedAtDesc();
	
	}
	
	@Test
	void 게시글_저장() {
		FoodBoard board = new FoodBoard(); 
		board.setTitle("스시집");
		
		foodBoardService.saveFood(board);
		
		verify(foodboardRepository, times(1)).save(board);
	}
	
	@Test
	void 게시글_ID로_조회_성공() {
		FoodBoard board = new FoodBoard(); 
		board.setTitle("돈코츠 라멘");
		
		when(foodboardRepository.findById(1L))
			.thenReturn(Optional.of(board));
		
		FoodBoard result = foodBoardService.findById(1L);
		
		assertNotNull(result);
		assertEquals("돈코츠 라멘", result.getTitle());
	}
	
	@Test
	void 게시글_ID로_조회_실패_예외발생() {
		when(foodboardRepository.findById(99L))
			.thenReturn(Optional.empty());
		assertThrows(RuntimeException.class, () -> foodBoardService.findById(99L));
	}
	
	@Test
	void 게시글_수정() {
		FoodBoard existing = new FoodBoard(); 
		existing.setTitle("기존 제목");
		existing.setContent("기존 내용");
		
		FoodBoard updated = new FoodBoard(); 
		updated.setTitle("수정된 제목");
		updated.setContent("수정된 내용");
		updated.setRegion("関東");
		updated.setPrefecture("東京都");
		updated.setRating(5);
		updated.setImageUrls("");
		
		when(foodboardRepository.findById(1L))
			.thenReturn(Optional.of(existing));
		
		foodBoardService.updateBoard(1L, updated);
		
		assertEquals("수정된 제목", existing.getTitle());
		assertEquals("수정된 내용", existing.getContent());
		verify(foodboardRepository, times(1)).save(existing);
		
		
		
	}
}
