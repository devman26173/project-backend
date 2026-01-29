package com.example.join.service;

import com.example.join.entity.FoodBoard;
import com.example.join.repository.FoodBoardRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        board.setImageUrls(updatedBoard.getImageUrls());
        foodboardRepository.save(board);
    }
 // 게시글 삭제
    public void deleteBoard(Long id) {
        foodboardRepository.deleteById(id);
    }
 // 지방별 조회
    public List<FoodBoard> findByRegion(String region) {
        // 해당 지방의 모든 도도부현 찾기
        List<String> prefectures = getPrefecturesByRegion(region);
        // 그 도도부현들의 게시글 모두 조회
        return foodboardRepository.findByPrefectureIn(prefectures);
    }

    // 지방별 도도부현 매핑
    private List<String> getPrefecturesByRegion(String region) {
        Map<String, List<String>> regionMap = new HashMap<>();
        regionMap.put("北海道・東北", List.of("北海道", "青森県", "岩手県", "宮城県", "秋田県", "山形県", "福島県"));
        regionMap.put("関東", List.of("東京都", "神奈川県", "千葉県", "埼玉県", "茨城県", "栃木県", "群馬県"));
        regionMap.put("中部", List.of("愛知県", "静岡県", "岐阜県", "三重県", "新潟県", "富山県", "石川県", "福井県", "山梨県", "長野県"));
        regionMap.put("近畿", List.of("大阪府", "兵庫県", "京都府", "滋賀県", "奈良県", "和歌山県"));
        regionMap.put("中国・四国", List.of("鳥取県", "島根県", "岡山県", "広島県", "山口県", "徳島県", "香川県", "愛媛県", "高知県"));
        regionMap.put("九州・沖縄", List.of("福岡県", "佐賀県", "長崎県", "熊本県", "大分県", "宮崎県", "鹿児島県", "沖縄県"));
        
        return regionMap.getOrDefault(region, new ArrayList<>());
    }

// 도도부현별 조회
    public List<FoodBoard> findByPrefecture(String prefecture) {
        return foodboardRepository.findByPrefecture(prefecture);
    }
    
    
}