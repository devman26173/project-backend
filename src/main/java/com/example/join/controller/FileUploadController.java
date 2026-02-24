package com.example.join.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.join.service.ImageUploadService;
import com.example.join.service.ImageUploadService.PresignedUpload;
import com.example.join.service.ImageUploadService.UploadedImage;

@RestController
@RequestMapping("/api/uploads")
public class FileUploadController {

    private final ImageUploadService imageUploadService;

    public FileUploadController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @PostMapping("/presign")
    public ResponseEntity<?> createPresignedUploadUrl(@RequestBody PresignRequest request) {
        try {
            PresignedUpload upload = imageUploadService.createPresignedUpload(
                    request.fileName(),
                    request.contentType(),
                    request.fileSize());
            return ResponseEntity.ok(upload);
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "dev環境ではpresignアップロードをサポートしていません"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        UploadedImage uploadedImage = imageUploadService.uploadImage(file);
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
