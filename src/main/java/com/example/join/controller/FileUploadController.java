//package com.example.join.controller;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api")
//public class FileUploadController {
//	
//	private final S3Client r2Client;
//	
//	@Value("${cloudflare.r2.bucket-name}")
//	private String bucketName;
//	
//	@Value("${cloudflare.r2.public-url}")
//	private String publicUrl; 
//	
//	public FileUploadController(S3Client r2Client) {
//		this.r2Client = r2Client; 
//	}
//	
//	@PostMapping("/upload")
//	public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
//		Map<String, String> response = new HashMap<>();
//		
//		try {
//			// 파일 검증
//			if (file.isEmpty()) {
//				response.put("error", "ファイルが空です");
//				return ResponseEntity.badRequest().body(response);
//			}
//			
//			// 이미지 파일인지 확인
//			String contentType = file.getContentType();
//			if (contentType == null || !contentType.startsWith("image/")) {
//				response.put("error", "画像ファイルのみアップロード可能です");
//				return ResponseEntity.badRequest().body(response);
//			}
//			
//			// 파일 크기 체크 (10MB)
//			if (file.getSize() > 10 * 1024 * 1024) {
//				response.put("error", "ファイルサイズは10MB以下にしてください");
//				return ResponseEntity.badRequest().body(response);
//			}
//			
//			// 고유한 key 생성
//			String originalFilename = file.getOriginalFilename();
//			String extension = "";
//			if (originalFilename != null && originalFilename.contains(".")) {
//				extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//			}
//			String key = "foodboard/" + UUID.randomUUID().toString() + extension;
//			
//			// R2에 업로드
//			r2Client.putObject(
//				PutObjectRequest.builder()
//					.bucket(bucketName)
//					.key(key)
//					.contentType(file.getContentType())
//					.build(),
//				RequestBody.fromBytes(file.getBytes())
//			);
//			
//			// key만 반환 (DB 저장용)
//			response.put("key", key);
//			
//			// URL도 같이 반환 (미리보기용)
//			response.put("url", publicUrl + "/" + key);
//			
//			return ResponseEntity.ok(response);
//			
//		} catch (Exception e) {
//			response.put("error", "アップロード中にエラーが発生しました");
//			e.printStackTrace();
//			return ResponseEntity.status(500).body(response);
//		}
//	}
//}