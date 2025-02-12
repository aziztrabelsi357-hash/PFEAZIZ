package com.example.PfaBack.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    private String title;
    private String content;  // The content of the article
    private String source;   // The source (URL or reference) of the article (optional)
}
