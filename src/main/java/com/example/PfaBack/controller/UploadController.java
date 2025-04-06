package com.example.PfaBack.controller;

import com.example.PfaBack.Services.RabiesDetectionService;
import com.example.PfaBack.Services.UploadHistoryService;
import com.example.PfaBack.Security.JwtUtil;
import com.example.PfaBack.models.UploadHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private RabiesDetectionService rabiesDetectionService;

    @Autowired
    private UploadHistoryService uploadHistoryService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract user ID from JWT token
            String userId = getAuthenticatedUserId(authHeader);


            File tempFile = Files.createTempFile("upload-", file.getOriginalFilename()).toFile();
            file.transferTo(tempFile);

            // Convert image to Base64
            byte[] fileBytes = Files.readAllBytes(tempFile.toPath());
            String imageBase64 = Base64.getEncoder().encodeToString(fileBytes);


            Map<String, Object> result = rabiesDetectionService.detectRabies(tempFile);


            UploadHistory uploadHistory = new UploadHistory();
            uploadHistory.setUserId(userId);
            uploadHistory.setImageBase64(imageBase64);
            uploadHistory.setDetectionResult(result);
            uploadHistory.setTimestamp(new Date());
            uploadHistoryService.saveUploadHistory(uploadHistory);


            tempFile.delete();

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = Map.of(
                    "message", "Error during detection: " + e.getMessage(),
                    "status", "error",
                    "confidence", 0.0f,
                    "symptom", "None",
                    "boundingBox", Map.of("x", 0.0f, "y", 0.0f, "width", 0.0f, "height", 0.0f)
            );
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    private String getAuthenticatedUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header is missing or invalid");
        }
        String token = authHeader.substring(7);
        return jwtUtil.extractEmail(token);
    }
}