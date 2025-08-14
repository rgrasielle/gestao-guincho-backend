package com.sistemaguincho.gestaoguincho.dto;

import com.sistemaguincho.gestaoguincho.entity.Motorista;
import com.sistemaguincho.gestaoguincho.enums.Disponibilidade;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MotoristaResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String cnh;
    private String telefone;
    private String email;
    private Disponibilidade disponibilidade;

}
