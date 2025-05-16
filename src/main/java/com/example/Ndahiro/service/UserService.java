package com.example.Ndahiro.service;

import com.example.Ndahiro.dto.JwtResponse;
import com.example.Ndahiro.dto.LoginRequest;
import com.example.Ndahiro.dto.RegisterRequest;
import com.example.Ndahiro.model.User;
import com.example.Ndahiro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }
        
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword()); // Store password as plain text
        user.setRole(registerRequest.getRole());
        
        return userRepository.save(user);
    }
    
    public User loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + loginRequest.getEmail()));
        
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        return user;
    }
} 