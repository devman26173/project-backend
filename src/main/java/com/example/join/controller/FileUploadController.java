package com.example.join.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @Value("${file.upload-dir:src/main/resources/static/uploads}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        
        try {
            // 파일이 비어있는지 확인
            if (file.isEmpty()) {
                response.put("error", "파일이 비어있습니다");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 이미지 파일인지 확인
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                response.put("error", "이미지 파일만 업로드 가능합니다");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 업로드 디렉토리 생성
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }
            
            // 고유한 파일명 생성 (UUID 사용)
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String savedFilename = UUID.randomUUID().toString() + extension;
            
            // 파일 저장
            Path path = Paths.get(uploadDir + File.separator + savedFilename);
            Files.write(path, file.getBytes());
            
            // 저장된 파일의 URL 반환
            String fileUrl = "/uploads/" + savedFilename;
            response.put("url", fileUrl);
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("error", "파일 업로드 실패: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}