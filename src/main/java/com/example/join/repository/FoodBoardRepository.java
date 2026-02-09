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

    List<FoodBoard> findTop10ByUser_UserIdOrderByCreatedAtDesc(Long userId);
    
 // 지역별 최신 게시물 1개씩 조회
    List<FoodBoard> findTop1ByPrefectureInOrderByCreatedAtDesc(List<String> prefectures);

 // 지역별 최신 게시물 조회
    FoodBoard findFirstByRegionOrderByCreatedAtDesc(String region);
    
    //검색 결과 조회
    List<FoodBoard> findByTitleContainingOrContentContainingOrderByCreatedAtDesc(String title, String content);
}

