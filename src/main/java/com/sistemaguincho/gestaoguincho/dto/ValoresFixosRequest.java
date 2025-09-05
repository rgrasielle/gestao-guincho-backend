package com.sistemaguincho.gestaoguincho.dto;

import lombok.Data;

@Data
public class ValoresFixosRequest {

    private Double valorQuilometragemPorKm;
    private Double valorQuilometragemSaida;
    private Double valorMotoristaPorKm;
    private Double valorMotoristaSaida;
    private Double valorHoraParada;
    private Double valorHoraTrabalhada;
    private Double valorDiaria;
}
