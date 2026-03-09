package com.example.join.controller;

import com.example.join.entity.FoodBoard;
import com.example.join.service.FoodBoardService;
import com.example.join.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
public class HomeController {

    private final FoodBoardService foodBoardService;
    private final AiService aiService;

    public HomeController(FoodBoardService foodBoardService, @Autowired(required = false) AiService aiService) {
        this.foodBoardService = foodBoardService;
        this.aiService = aiService;
    }

    @GetMapping("/")
    public String home(Model model) {
    	List<String> prefectures = List.of("北海道・東北","関東","中部","近畿","中国・四国","九州・沖縄");
		model.addAttribute("prefectures", prefectures);
    	model.addAttribute("message", "Hello Thymeleaf!");
        return "home"; 
    }

    @GetMapping("/boards")
    public String boards(Model model) {
        Map<String, FoodBoard> regionPosts = foodBoardService.getFirstPostByRegion();
        model.addAttribute("regionPosts", regionPosts);
        return "boards";
    }
    
    public void prefecture(Model model) {		
		List<String> prefectures = List.of("北海道・東北","関東","中部","近畿","中国・四国","九州・沖縄");
		model.addAttribute("prefectures", prefectures);
	}
    
    @PostMapping("/api/gemini/ask")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> askGemini(@RequestBody Map<String, String> request) {
        if (aiService == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "AI機能は現在利用できません。APIキーが設定されていません。");
            return ResponseEntity.status(503).body(errorResponse);
        }
        try {
            String question = request.get("question");
            if (question == null || question.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "質問が空です。質問を入力してください。");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            AiService.AiReply response = aiService.generateResponse(question.trim());
            
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("answer", response.answer());
            successResponse.put("keywords", response.keywords());
            return ResponseEntity.ok(successResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "서버에서 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

}
