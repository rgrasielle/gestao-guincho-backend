package com.sistemaguincho.gestaoguincho.auth.controller;

import com.sistemaguincho.gestaoguincho.auth.dto.ChangePasswordRequest;
import com.sistemaguincho.gestaoguincho.auth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok("Olá, " + userDetails.getUsername());
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest request) { // Adicionado @Valid

        userService.changePassword(userDetails.getUsername(), request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal UserDetails userDetails) {

        userService.deleteAccount(userDetails.getUsername());

        return ResponseEntity.noContent().build();
    }
}
