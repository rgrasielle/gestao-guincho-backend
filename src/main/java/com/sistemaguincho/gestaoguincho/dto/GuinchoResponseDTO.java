package com.sistemaguincho.gestaoguincho.dto;

import com.sistemaguincho.gestaoguincho.enums.Disponibilidade;
import lombok.Data;

@Data
public class GuinchoResponseDTO {

    private Long id;
    private String modelo;
    private String placa;
    private String tipo;
    private Double capacidade;
    private Disponibilidade disponibilidade;

}
