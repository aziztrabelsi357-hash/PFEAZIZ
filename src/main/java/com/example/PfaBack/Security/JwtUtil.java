package com.example.PfaBack.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String secret = "SECRET_KEY_SECRET_KEY_SECRET_KEY_SECRET_KEY";
    private final long jwtExpirationInMs = 3600000; // 1 hour in milliseconds
    private final Key key;

    public JwtUtil() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email) {
        System.out.println("Generating token for email: " + email);
        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(key)
                .compact();
        System.out.println("Generated token: " + token);
        return token;
    }

    public String extractEmail(String token) {
        try {
            System.out.println("Extracting email from token: " + token);
            String email = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            System.out.println("Extracted email: " + email);
            return email;
        } catch (JwtException e) {
            System.out.println("Failed to extract email: " + e.getMessage());
            throw new RuntimeException("Invalid JWT token: " + e.getMessage());
        }
    }

    public boolean validateToken(String token) {
        try {
            System.out.println("Validating token: " + token);
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            System.out.println("Token validation successful");
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token is expired: " + e.getMessage());
            return false;
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }
}