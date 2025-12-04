package com.edutrack.backend.controller;


import com.edutrack.backend.dto.AuthRequest;
import com.edutrack.backend.dto.AuthResponse;
import com.edutrack.backend.dto.GoogleAuthRequest;
import com.edutrack.backend.service.AuthService;
import com.edutrack.backend.service.GoogleAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final GoogleAuthService googleAuthService;
//    private final PasswordEncoder passwordEncoder;
//
//    // Временный метод для генерации хэша
//    @GetMapping("/generate-hash")
//    public String generateHash(@RequestParam String password) {
//        return passwordEncoder.encode(password);
//    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.authenticate(request);
        if (response.getSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleAuth(@Valid @RequestBody GoogleAuthRequest request) {
        AuthResponse response = googleAuthService.authenticateWithGoogle(request.getGoogleToken());
        if (response.getSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }
}