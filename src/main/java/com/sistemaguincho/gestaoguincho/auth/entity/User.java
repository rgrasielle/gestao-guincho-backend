package com.sistemaguincho.gestaoguincho.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // 🚨 Se no futuro quiser roles/perfis, você pode adicionar uma lista de papéis
    // Por enquanto vamos deixar vazio (usuário comum)

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Sem roles por enquanto → retorna lista vazia
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // conta nunca expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // conta nunca bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // senha nunca expira
    }

    @Override
    public boolean isEnabled() {
        return true; // usuário sempre ativo
    }
}
