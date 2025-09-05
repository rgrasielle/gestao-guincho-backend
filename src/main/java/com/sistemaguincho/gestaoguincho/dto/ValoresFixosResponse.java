package com.sistemaguincho.gestaoguincho.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValoresFixosResponse {

    private Double valorQuilometragemPorKm;
    private Double valorQuilometragemSaida;
    private Double valorMotoristaPorKm;
    private Double valorMotoristaSaida;
    private Double valorHoraParada;
    private Double valorHoraTrabalhada;
    private Double valorDiaria;
}
