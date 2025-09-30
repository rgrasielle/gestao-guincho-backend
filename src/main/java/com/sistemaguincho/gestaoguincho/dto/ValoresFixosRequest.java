package com.sistemaguincho.gestaoguincho.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ValoresFixosRequest {

    private BigDecimal valorQuilometragemPorKm;
    private BigDecimal  valorQuilometragemSaida;
    private BigDecimal  valorMotoristaPorKm;
    private BigDecimal  valorMotoristaSaida;
    private BigDecimal  valorHoraParada;
    private BigDecimal  valorHoraTrabalhada;
    private BigDecimal  valorDiaria;
}
