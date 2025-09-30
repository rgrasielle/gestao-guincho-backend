package com.sistemaguincho.gestaoguincho.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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

    private BigDecimal valorQuilometragemPorKm;

    private BigDecimal  valorQuilometragemSaida;

    private BigDecimal  valorMotoristaPorKm;

    private BigDecimal  valorMotoristaSaida;

    private BigDecimal  valorHoraParada;

    private BigDecimal  valorHoraTrabalhada;

    private BigDecimal  valorDiaria;

}

