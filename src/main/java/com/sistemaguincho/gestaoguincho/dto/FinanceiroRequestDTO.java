package com.sistemaguincho.gestaoguincho.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinanceiroRequestDTO {

    private Long chamadoId; // vincula o financeiro ao chamado

    private List<CustoQuilometragemDTO> custosQuilometragem;
    private List<CustoMotoristaDTO> custosMotorista;
    private List<CustoPedagioDTO> custosPedagio;
    private List<CustoPatinsDTO> custosPatins;
    private List<CustoHoraParadaDTO> custosHoraParada;
    private List<CustoHoraTrabalhadaDTO> custosHoraTrabalhada;
    private List<CustoDiariaDTO> custosDiaria;
    private List<CustoRodaExtraDTO> custosRodaExtra;
    private List<CustoExcedenteDTO> custosExcedente;
}
