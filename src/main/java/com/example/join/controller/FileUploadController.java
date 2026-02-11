package com.example.join.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.join.service.R2UploadService;
import com.example.join.service.R2UploadService.PresignedUpload;

@RestController
@RequestMapping("/api/uploads")
public class FileUploadController {

    private final R2UploadService r2UploadService;

    public FileUploadController(R2UploadService r2UploadService) {
        this.r2UploadService = r2UploadService;
    }

    @PostMapping("/presign")
    public ResponseEntity<?> createPresignedUploadUrl(@RequestBody PresignRequest request) {
        try {
            PresignedUpload upload = r2UploadService.createPresignedUpload(
                    request.fileName(),
                    request.contentType(),
                    request.fileSize());
            return ResponseEntity.ok(upload);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    public record PresignRequest(String fileName, String contentType, long fileSize) {
    }
}

