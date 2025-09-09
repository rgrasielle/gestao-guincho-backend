package com.sistemaguincho.gestaoguincho.auth.service;

import com.sistemaguincho.gestaoguincho.auth.dto.AuthResponse;
import com.sistemaguincho.gestaoguincho.auth.dto.LoginRequest;
import com.sistemaguincho.gestaoguincho.auth.dto.RegisterRequest;
import com.sistemaguincho.gestaoguincho.auth.entity.User;
import com.sistemaguincho.gestaoguincho.auth.repository.UserRepository;
import com.sistemaguincho.gestaoguincho.auth.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Usuário já existe");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Senhas não conferem");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        // Criar UserDetails a partir do User
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>() // authorities vazias, ou roles se tiver
        );

        String token = jwtTokenProvider.generateToken(userDetails);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Senha inválida");
        }

        // Criar UserDetails a partir do User
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>() // authorities vazias
        );

        String token = jwtTokenProvider.generateToken(userDetails);
        return new AuthResponse(token);
    }
}
