package com.chatbot.controller;

import com.chatbot.dto.AuthRequest;
import com.chatbot.dto.AuthResponse;
import com.chatbot.entity.User;
import com.chatbot.repository.UserRepository;
import com.chatbot.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // 1. Register API
    @PostMapping("/register")
    public String register(@RequestBody AuthRequest request) {
        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Hash the password
        userRepository.save(user);

        return "User registered successfully!";
    }

    // 2. Login API
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        // This validates the email and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // If validation passed, generate the token
        String token = jwtUtil.generateToken(request.getEmail());
        return new AuthResponse(token);
    }
}