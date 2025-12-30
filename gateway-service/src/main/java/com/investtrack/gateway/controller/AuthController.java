package com.investtrack.gateway.controller;

import com.investtrack.gateway.dto.LoginRequest;
import com.investtrack.gateway.dto.LoginResponse;
import com.investtrack.gateway.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Controller - Handles login and JWT token generation
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtUtil jwtUtil;

    // Hardcoded users for demo purposes
    private static final Map<String, String> DEMO_USERS = new HashMap<>();
    
    static {
        DEMO_USERS.put("user1", "password");
        DEMO_USERS.put("user2", "password");
        DEMO_USERS.put("admin", "admin123");
    }

    /**
     * Login endpoint - Validates credentials and returns JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for user: {}", loginRequest.getUsername());
        
        // Validate credentials (simplified for demo)
        String storedPassword = DEMO_USERS.get(loginRequest.getUsername());
        
        if (storedPassword == null || !storedPassword.equals(loginRequest.getPassword())) {
            log.warn("Invalid credentials for user: {}", loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, null, "Invalid username or password"));
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(loginRequest.getUsername());
        log.info("JWT token generated successfully for user: {}", loginRequest.getUsername());
        
        return ResponseEntity.ok(new LoginResponse(
                token,
                loginRequest.getUsername(),
                "Login successful"
        ));
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "gateway-service");
        return ResponseEntity.ok(response);
    }
}
