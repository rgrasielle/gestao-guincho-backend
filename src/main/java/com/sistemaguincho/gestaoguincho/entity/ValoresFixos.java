package com.sistemaguincho.gestaoguincho.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "valores_fixos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValoresFixos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double valorQuilometragemPorKm;

    private Double valorQuilometragemSaida;

    private Double valorMotoristaPorKm;

    private Double valorMotoristaSaida;

    private Double valorHoraParada;

    private Double valorHoraTrabalhada;

    private Double valorDiaria;

}

