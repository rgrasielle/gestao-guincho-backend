package com.sistemaguincho.gestaoguincho.dto;

import com.sistemaguincho.gestaoguincho.enums.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChamadoStatusUpdateDTO {

    @NotBlank(message = "O status é obrigatório")
    private Status status;  // Valores possíveis: "Aguardando", "Em andamento", "Finalizado"

}
