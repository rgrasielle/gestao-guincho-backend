package com.sistemaguincho.gestaoguincho.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "motorista")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Motorista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String motoristaNome;
    private String motoristaCpf;
    private String motoristaCnh;
    private String motoristaTelefone;
    private String motoristaEmail;

}
