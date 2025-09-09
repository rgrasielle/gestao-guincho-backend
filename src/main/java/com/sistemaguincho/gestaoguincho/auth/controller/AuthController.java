package com.sistemaguincho.gestaoguincho.auth.controller;

import com.sistemaguincho.gestaoguincho.auth.dto.AuthResponse;
import com.sistemaguincho.gestaoguincho.auth.dto.LoginRequest;
import com.sistemaguincho.gestaoguincho.auth.dto.RegisterRequest;
import com.sistemaguincho.gestaoguincho.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
