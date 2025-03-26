package com.example.PfaBack.controller;

import com.example.PfaBack.Services.RabiesDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private RabiesDetectionService rabiesDetectionService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            File tempFile = Files.createTempFile("upload-", file.getOriginalFilename()).toFile();
            file.transferTo(tempFile);
            Map<String, Object> result = rabiesDetectionService.detectRabies(tempFile);
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
}