package com.sistemaguincho.gestaoguincho.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ValoresFixosResponse {

    private BigDecimal  valorQuilometragemPorKm;
    private BigDecimal  valorQuilometragemSaida;
    private BigDecimal  valorMotoristaPorKm;
    private BigDecimal  valorMotoristaSaida;
    private BigDecimal  valorHoraParada;
    private BigDecimal  valorHoraTrabalhada;
    private BigDecimal valorDiaria;
}
