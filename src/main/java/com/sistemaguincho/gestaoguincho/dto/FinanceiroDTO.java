package com.sistemaguincho.gestaoguincho.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinanceiroDTO {

    private Long id;
    private Long chamadoId;

    // --- Pagamento Quilometragem ---
    private BigDecimal pagamentoKmRodados;
    private BigDecimal pagamentoValorKm;
    private BigDecimal pagamentoQuantidadeSaida;
    private BigDecimal pagamentoValorSaida;
    private BigDecimal totalPagamento;

    // --- Acerto com Motorista ---
    private BigDecimal acertoKmRodados;
    private BigDecimal acertoValorKm;
    private BigDecimal acertoQuantidadeSaida;
    private BigDecimal acertoValorSaida;
    private BigDecimal totalAcerto;

    // --- Pedágios ---
    private List<CustoPedagioDTO> custosPedagio;

    // --- Patins ---
    private String descricaoPatins;
    private BigDecimal valorPatins;

    // --- Hora Parada ---
    private BigDecimal horasParadas;
    private BigDecimal valorHoraParada;
    private BigDecimal totalHoraParada;

    // --- Hora Trabalhada ---
    private BigDecimal horasTrabalhadas;
    private BigDecimal valorHoraTrabalhada;
    private BigDecimal totalHoraTrabalhada;

    // --- Diárias ---
    private LocalDate entradaDiarias;
    private LocalDate saidaDiarias;
    private Integer estadiasDiarias;
    private BigDecimal valorDiaria;
    private BigDecimal totalDiarias;

    // --- Rodas Extras ---
    private String descricaoRodas;
    private BigDecimal valorRodas;

    // --- Valor Excedente ---
    private BigDecimal excedente;
    private String observacoesExcedente;

    // --- Totais Finais ---

    private BigDecimal subtotal;
    private BigDecimal desconto;
    private BigDecimal total;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustoPedagioDTO {
        private Integer quantidadePedagio;
        private BigDecimal valorPedagio;
    }
}
