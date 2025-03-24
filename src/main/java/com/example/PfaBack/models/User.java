package com.example.PfaBack.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Set;
import java.util.Date;

@AllArgsConstructor
@Document(collection = "users")
@Data
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private Set<String> roles;
    private String resetToken;         // New field for reset token
    private Date resetTokenExpiry;     // New field for token expiration
}