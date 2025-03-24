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

    // Generate a JWT token for the given email
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(key)
                .compact();
    }


    public String extractEmail(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT token: " + e.getMessage());
        }
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
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