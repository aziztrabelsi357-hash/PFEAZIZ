package com.example.PfaBack.models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "care-tips")
public class CareTip {
    @Id
    private String id;
    private String title;
    private String description;
    private Article article;
}