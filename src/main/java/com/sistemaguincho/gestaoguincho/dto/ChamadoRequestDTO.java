package com.sistemaguincho.gestaoguincho.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChamadoRequestDTO {

    private ClienteDTO cliente;
    private VeiculoDTO veiculo;
    private EnderecoDTO origem;
    private EnderecoDTO destino;
    private ServicoDTO servico;
    private String observacoes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClienteDTO {
        private String nome;
        private String cpfCnpj;
        private String telefone;
        private String email;
        private String solicitante;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VeiculoDTO {
        private String modelo;
        private Integer ano;
        private String cor;
        private String placa;
        private String observacoes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnderecoDTO {
        private String cep;
        private String cidade;
        private String estado;
        private String bairro;
        private String logradouro;
        private String numero;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServicoDTO {
        private String seguradora;
        private String sinistro;
        private LocalDate dataAcionamento;

        @JsonFormat(pattern = "HH:mm:ss") // Define que o Jackson deve esperar apenas a hora
        private LocalTime horaAcionamento;

        private String tipoServico;
        private Long guinchoId;
        private Long motoristaId;
    }
}

