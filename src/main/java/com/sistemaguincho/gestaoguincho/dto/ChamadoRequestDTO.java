package com.sistemaguincho.gestaoguincho.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChamadoRequestDTO {

    // 📌 Dados do serviço
    @NotBlank(message = "A seguradora é obrigatória.")
    private String seguradora;

    @NotBlank(message = "O número do sinistro é obrigatório.")
    private String sinistro;

    @NotNull(message = "A data do acionamento é obrigatória.")
    private LocalDate dataAcionamento;

    @NotNull(message = "A hora do acionamento é obrigatória.")
    private LocalTime hora;

    @NotBlank(message = "O tipo de serviço é obrigatório.")
    private String tipoServico;

    @NotNull(message = "O ID do motorista é obrigatório.")
    private Long motoristaId; // Vamos armazenar o ID, não o objeto inteiro

    // 📌 Origem
    @NotBlank private String origemCep;
    @NotBlank private String origemCidade;
    @NotBlank private String origemEstado;
    @NotBlank private String origemBairro;
    @NotBlank private String origemLogradouro;
    @NotBlank private String origemNumero;

    // 📌 Destino
    @NotBlank private String destinoCep;
    @NotBlank private String destinoCidade;
    @NotBlank private String destinoEstado;
    @NotBlank private String destinoBairro;
    @NotBlank private String destinoLogradouro;
    @NotBlank private String destinoNumero;

    // 📌 Dados do veículo
    @NotBlank private String veiculoModelo;
    @NotNull private Integer veiculoAno;
    @NotBlank private String veiculoCor;
    @NotBlank private String veiculoPlaca;

    @Size(max = 500)
    private String veiculoObservacoes;

    // 📌 Dados do cliente
    @NotBlank private String clienteNome;
    @NotBlank private String clienteCpfCnpj;
    @NotBlank private String clienteTelefone;
    @NotBlank private String clienteEmail;
    @NotBlank private String clienteSolicitante;

    // 📌 Campo de observações gerais
    private String observacoes;
}
