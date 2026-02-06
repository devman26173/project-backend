//package com.example.join.controller;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api")
//public class FileUploadController {
//	
//	private final R2Service r2Service;
//	
//	public FileUploadController(R2Service r2Service) {
//		this.r2Service = r2Service;
//	}
//	
//    @PostMapping("/upload")
//    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
//    	@RequestParam(value = "file", required = false) MultipartFile file) {
//    	Map<String, String> response = new HashMap<>();
//        
//        try {
//            // 파일이 비어있는지 확인
//        	if (file == null || file.isEmpty()) {
//        	    response.put("url", ""); // 빈 주소를 돌려줌
//        	    return ResponseEntity.ok(response); // 에러가 아니라 '성공'으로 응답
//        	}
//            
//            // 파일 크기 체크 (10MB)
//            if(file.getSize() > 10 * 1024 * 1024) {
//            	response.put("error", "ファイルサイズは10MB以下にしてください");
//            	return ResponseEntity.badRequest().body(response);
//            }
//            // 이미지 파일인지 확인
//            String contentType = file.getContentType();
//            if (contentType == null || !contentType.startsWith("image/")) {
//                response.put("error", "画像ファイルのみアップロード可能です");
//                return ResponseEntity.badRequest().body(response);
//            }
//            
//            String fileUrl = r2Service.uploadFile(file);
//            response.put("url", fileUrl);
//            return ResponseEntity.ok(response);
//            
//        } catch (Exception e) {
//            response.put("error", "アップロード失敗: " + e.getMessage());
//            return ResponseEntity.status(500).body(response);
//        }
//    }
//}
//}