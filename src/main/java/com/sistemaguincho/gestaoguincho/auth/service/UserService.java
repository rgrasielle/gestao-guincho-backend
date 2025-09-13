package com.sistemaguincho.gestaoguincho.auth.service;

import com.sistemaguincho.gestaoguincho.auth.dto.ChangePasswordRequest;
import com.sistemaguincho.gestaoguincho.auth.entity.User;
import com.sistemaguincho.gestaoguincho.auth.repository.UserRepository; // Importe seu UserRepository
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void changePassword(String username, ChangePasswordRequest request) {
        // 1. Encontra o usuário no banco de dados
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // 2. Verifica se a senha atual fornecida corresponde à senha salva
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Senha atual incorreta.");
        }

        // 3. Verifica se a nova senha e a confirmação são iguais
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("A nova senha e a confirmação não coincidem.");
        }

        // 4. Criptografa e define a nova senha
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // 5. Salva o usuário com a senha atualizada
        userRepository.save(user);
    }

    @Transactional
    public void deleteAccount(String username) {
        // 1. Encontra o usuário no banco de dados
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // 2. Deleta o usuário
        userRepository.delete(user);
    }
}
