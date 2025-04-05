package com.example.PfaBack.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "upload_history")
public class UploadHistory {
    @Id
    private String id;
    private String userId;
    private String imageBase64;
    private Map<String, Object> detectionResult;
    private Date timestamp;
}