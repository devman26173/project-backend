package com.example.join.service;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
public class R2UploadService {

    private static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024;
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg", "image/png", "image/webp", "image/gif", "image/heic", "image/heif");

    private final S3Presigner s3Presigner;
    private final String bucketName;
    private final String publicUrl;

    public R2UploadService(
            S3Presigner s3Presigner,
            @Value("${cloudflare.r2.bucket-name}") String bucketName,
            @Value("${cloudflare.r2.public-url}") String publicUrl) {
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
        this.publicUrl = publicUrl;
    }

    public PresignedUpload createPresignedUpload(String originalFilename, String contentType, long fileSize) {
        validateUploadRequest(contentType, fileSize);

        String extension = extractExtension(originalFilename);
        String key = "foodboard/" + UUID.randomUUID() + extension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(5))
                        .putObjectRequest(putObjectRequest)
                        .build());

        return new PresignedUpload(key, presignedRequest.url().toString(), buildPublicUrl(key));
    }

    public String normalizeImageUrls(String imageUrlsCsv) {
        if (!StringUtils.hasText(imageUrlsCsv)) {
            return null;
        }

        List<String> validUrls = Arrays.stream(imageUrlsCsv.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .filter(url -> url.startsWith(publicUrl + "/foodboard/"))
                .distinct()
                .limit(5)
                .collect(Collectors.toList());

        if (validUrls.isEmpty()) {
            return null;
        }
        return String.join(",", validUrls);
    }

    private void validateUploadRequest(String contentType, long fileSize) {
        if (!StringUtils.hasText(contentType) || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("画像ファイルのみアップロード可能です");
        }
        if (fileSize <= 0 || fileSize > MAX_FILE_SIZE_BYTES) {
            throw new IllegalArgumentException("ファイルサイズは10MB以下にしてください");
        }
    }

    private String extractExtension(String originalFilename) {
        if (!StringUtils.hasText(originalFilename) || !originalFilename.contains(".")) {
            return ".jpg";
        }
        return originalFilename.substring(originalFilename.lastIndexOf('.'));
    }

    private String buildPublicUrl(String key) {
        return publicUrl + "/" + key;
    }

    public record PresignedUpload(String key, String uploadUrl, String publicUrl) {
    }
}
