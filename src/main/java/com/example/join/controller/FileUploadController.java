package com.example.join.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.join.service.R2UploadService;
import com.example.join.service.R2UploadService.PresignedUpload;

@RestController
@RequestMapping("/api/uploads")
public class FileUploadController {

    private final R2UploadService r2UploadService;
    private final Path uploadDir;

    public FileUploadController(
            R2UploadService r2UploadService,
            @Value("${file.upload-dir:src/main/resources/static/uploads}") String uploadDir) {
        this.r2UploadService = r2UploadService;
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
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

    @PostMapping("/local")
    public ResponseEntity<?> uploadLocalFile(@RequestParam("file") MultipartFile file) {
        try {
            r2UploadService.validateLocalUpload(file.getContentType(), file.getSize());

            Files.createDirectories(uploadDir);
            String extension = extractExtension(file.getOriginalFilename());
            String filename = UUID.randomUUID() + extension;
            Path targetPath = uploadDir.resolve(filename);

            file.transferTo(targetPath.toFile());

            return ResponseEntity.ok(Map.of("publicUrl", "/uploads/" + filename));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "ローカルアップロードに失敗しました"));
        }
    }

    private String extractExtension(String originalFilename) {
        if (!StringUtils.hasText(originalFilename) || !originalFilename.contains(".")) {
            return ".jpg";
        }
        return originalFilename.substring(originalFilename.lastIndexOf('.'));
    }

    public record PresignRequest(String fileName, String contentType, long fileSize) {
    }
}
