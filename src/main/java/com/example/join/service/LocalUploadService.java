package com.example.join.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("dev")
public class LocalUploadService implements ImageUploadService {

    private static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024;
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg", "image/jpg", "image/png", "image/webp", "image/gif", "image/heic", "image/heif");

    private final Path uploadRootPath;
    private final String publicBaseUrl;

    public LocalUploadService(
            @Value("${file.upload-dir:uploads}") String uploadDir,
            @Value("${app.upload.public-base-url:/uploads}") String publicBaseUrl) {
        this.uploadRootPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.publicBaseUrl = normalizeBaseUrl(publicBaseUrl);
    }

    @Override
    public UploadedImage uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("画像ファイルが必要です");
        }

        String contentType = file.getContentType();
        long fileSize = file.getSize();
        validateUploadRequest(contentType, fileSize);

        String extension = extractExtension(file.getOriginalFilename());
        String key = "foodboard/" + UUID.randomUUID() + extension;
        Path targetPath = uploadRootPath.resolve(key).normalize();
        ensureUploadPath(targetPath);

        try {
            Files.copy(file.getInputStream(), targetPath);
        } catch (IOException e) {
            throw new IllegalStateException("ローカル画像アップロードに失敗しました", e);
        }

        return new UploadedImage(key, buildPublicUrl(key));
    }

    @Override
    public PresignedUpload createPresignedUpload(String originalFilename, String contentType, long fileSize) {
        throw new UnsupportedOperationException("Presigned upload is not available in dev profile");
    }

    @Override
    public String normalizeImageUrls(String imageUrlsCsv) {
        if (!StringUtils.hasText(imageUrlsCsv)) {
            return null;
        }

        List<String> validUrls = Arrays.stream(imageUrlsCsv.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .filter(url -> url.startsWith(publicBaseUrl + "/foodboard/"))
                .distinct()
                .limit(5)
                .collect(Collectors.toList());

        return validUrls.isEmpty() ? null : String.join(",", validUrls);
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
        return publicBaseUrl + "/" + key;
    }

    private String normalizeBaseUrl(String baseUrl) {
        if (!StringUtils.hasText(baseUrl)) {
            return "/uploads";
        }
        return baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    private void ensureUploadPath(Path targetPath) {
        if (!targetPath.startsWith(uploadRootPath)) {
            throw new IllegalArgumentException("無効なファイルパスです");
        }
        try {
            Files.createDirectories(targetPath.getParent());
        } catch (IOException e) {
            throw new IllegalStateException("アップロードディレクトリを作成できません", e);
        }
    }
}
