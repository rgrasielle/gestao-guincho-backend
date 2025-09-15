package com.sistemaguincho.gestaoguincho.dto;

import com.sistemaguincho.gestaoguincho.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChamadoResponseDTO {

    // Dados principais
    private Long id;
    private Status status;
    private String observacoes;

    // Cliente
    private String clienteNome;
    private String clienteTelefone;
    private String clienteEmail;
    private String clienteCpfCnpj;
    private String clienteSolicitante;

    // Veiculo
    private String veiculoModelo;
    private Integer veiculoAno;
    private String veiculoCor;
    private String veiculoPlaca;
    private String veiculoObservacoes;

    // Origem
    private String origemCep;
    private String origemCidade;
    private String origemEstado;
    private String origemBairro;
    private String origemLogradouro;
    private String origemNumero;

    // Destino
    private String destinoCep;
    private String destinoCidade;
    private String destinoEstado;
    private String destinoBairro;
    private String destinoLogradouro;
    private String destinoNumero;

    // Serviço
    private String seguradora;
    private String sinistro;
    private LocalDate dataServico;
    private LocalTime hora;
    private String tipoServico;

    // Campos de relacionamento
    private Long guinchoId;
    private Long motoristaId;
    private String motoristaNome;
    private String guinchoDescricao;

    // Campos formatados para a listagem
    private String origemFormatada;
    private String destinoFormatado;

    // Campo financeiro
    private BigDecimal valorFinal;

    // Campo de data de criação para o cálculo de "tempo atrás"
    private OffsetDateTime createdAt;
}