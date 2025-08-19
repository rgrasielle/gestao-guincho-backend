package com.sistemaguincho.gestaoguincho.dto;

import com.sistemaguincho.gestaoguincho.enums.Disponibilidade;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuinchoDisponibilidadeUpdateDTO {

    @NotNull(message = "A disponibilidade é obrigatória")
    private Disponibilidade disponibilidade;
}
