package com.sistemaguincho.gestaoguincho.dto;

import com.sistemaguincho.gestaoguincho.enums.Disponibilidade;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GuinchoResponseDTO {

    private Long id;
    private String modelo;
    private String placa;
    private String tipo;
    private BigDecimal capacidade;
    private Disponibilidade disponibilidade;

}
