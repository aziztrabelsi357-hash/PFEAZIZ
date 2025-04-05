package com.example.PfaBack.controller;

import com.example.PfaBack.Services.UploadHistoryService;
import com.example.PfaBack.Security.JwtUtil;
import com.example.PfaBack.models.UploadHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class UploadHistoryController {

    @Autowired
    private UploadHistoryService uploadHistoryService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<UploadHistory>> getUserUploadHistory(@RequestHeader("Authorization") String authHeader) {
        String userId = getAuthenticatedUserId(authHeader);
        List<UploadHistory> history = uploadHistoryService.getUploadHistoryByUserId(userId);
        return ResponseEntity.ok(history);
    }

    private String getAuthenticatedUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header is missing or invalid");
        }
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        return jwtUtil.extractEmail(token);
    }
}