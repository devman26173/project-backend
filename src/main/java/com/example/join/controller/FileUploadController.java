package com.example.join.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.join.service.R2UploadService;
import com.example.join.service.R2UploadService.PresignedUpload;
import com.example.join.service.R2UploadService.UploadedImage;

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

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        UploadedImage uploadedImage = r2UploadService.uploadImage(file);
        return ResponseEntity.ok(uploadedImage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleUploadFailure(IllegalStateException e) {
        return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
    }

    public record PresignRequest(String fileName, String contentType, long fileSize) {
    }
}
