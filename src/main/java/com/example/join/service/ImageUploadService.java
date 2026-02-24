package com.example.join.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {

    UploadedImage uploadImage(MultipartFile file);

    PresignedUpload createPresignedUpload(String originalFilename, String contentType, long fileSize);

    String normalizeImageUrls(String imageUrlsCsv);

    record PresignedUpload(String key, String uploadUrl, String publicUrl) {
    }

    record UploadedImage(String key, String publicUrl) {
    }
}
