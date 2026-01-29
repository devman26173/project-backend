package com.example.join.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.join.entity.FoodBoard;


public interface FoodBoardRepository extends JpaRepository<FoodBoard, Long> {
	 // 지방별 조회
    List<FoodBoard> findByRegion(String region);
    
    // 도도부현별 조회
    List<FoodBoard> findByPrefecture(String prefecture);
    
 // 여러 도도부현 조회 (추가)
    List<FoodBoard> findByPrefectureIn(List<String> prefectures);
}

