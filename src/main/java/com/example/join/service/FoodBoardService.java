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
    public void saveFood(FoodBoard foodBoard) {
        foodboardRepository.save(foodBoard);
    }
 // ID로 게시글 찾기
    public FoodBoard findById(Long id) {
    	return foodboardRepository.findById(id)
    			.orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    }
 // 게시글 수정  
    public void updateBoard(Long id, FoodBoard updatedBoard) {
    	FoodBoard board = findById(id);
    	board.setTitle(updatedBoard.getTitle());
        board.setRegion(updatedBoard.getRegion());
        board.setPrefecture(updatedBoard.getPrefecture());
        board.setRating(updatedBoard.getRating());
        board.setContent(updatedBoard.getContent());
        board.setImageUrl(updatedBoard.getImageUrl());
        foodboardRepository.save(board);
    }
 // 게시글 삭제
    public void deleteBoard(Long id) {
        foodboardRepository.deleteById(id);
    }
}