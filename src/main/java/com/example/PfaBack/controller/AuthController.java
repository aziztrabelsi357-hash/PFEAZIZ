package com.example.PfaBack.controller;

import com.example.PfaBack.models.AuthRequest;
import com.example.PfaBack.models.User;
import com.example.PfaBack.repository.UserRepository;
import com.example.PfaBack.Security.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of("USER"));
        }
        userRepository.save(user);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent() && passwordEncoder.matches(request.getPassword(), userOptional.get().getPassword())) {
            return jwtUtil.generateToken(userOptional.get().getEmail());
        }
        return "Invalid credentials!";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody EmailRequest emailRequest) {
        Optional<User> userOptional = userRepository.findByEmail(emailRequest.getEmail());
        if (!userOptional.isPresent()) {
            return "Email not found!";
        }

        User user = userOptional.get();
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(new Date(System.currentTimeMillis() + 3600000)); // 1-hour expiry
        userRepository.save(user);

        // Send email with reset link
        String resetLink = "http://localhost:5173/reset-password?token=" + resetToken;
        sendResetEmail(user.getEmail(), resetLink);

        return "Password reset link sent to your email!";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {
        Optional<User> userOptional = userRepository.findByResetToken(request.getToken());
        if (!userOptional.isPresent() || userOptional.get().getResetTokenExpiry().before(new Date())) {
            return "Invalid or expired token!";
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        return "Password reset successfully!";
    }

    private void sendResetEmail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("Click the link to reset your password: " + resetLink);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
            System.out.println("Reset link (for testing): " + resetLink);
        }
    }
}

@Data
class EmailRequest {
    private String email;
}

@Data
class ResetPasswordRequest {
    private String token;
    private String newPassword;
}