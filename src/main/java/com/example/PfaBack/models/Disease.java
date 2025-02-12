package com.example.PfaBack.models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "diseases")
public class Disease {
    @Id
    private String id;
    private String name;
    private String description;
    private List<String> symptoms;
    private List<String> treatments;
    private List<Article> articles;
}

