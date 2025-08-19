package com.sistemaguincho.gestaoguincho.dto;

import com.sistemaguincho.gestaoguincho.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChamadoResponseDTO {

    private Long id;
    private LocalDate dataAcionamento;
    private String sinistro;
    private String seguradora;
    private String motorista;
    private String tipoServico;
    private Status status;

}
