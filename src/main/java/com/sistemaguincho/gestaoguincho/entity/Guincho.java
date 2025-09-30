package com.sistemaguincho.gestaoguincho.entity;

import com.sistemaguincho.gestaoguincho.enums.Disponibilidade;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "guincho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guincho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelo;

    @Column(unique = true, nullable = false)
    private String placa;

    private String tipo;

    private BigDecimal capacidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Disponibilidade disponibilidade;

    @CreationTimestamp
    private LocalDateTime createdAt;
}

