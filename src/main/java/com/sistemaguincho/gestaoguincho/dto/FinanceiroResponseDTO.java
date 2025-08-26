package com.sistemaguincho.gestaoguincho.dto;

import com.sistemaguincho.gestaoguincho.dto.custo.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinanceiroResponseDTO {

    private Long id;
    private Long chamadoId;

    private List<CustoQuilometragemDTO> custosQuilometragem;
    private List<CustoMotoristaDTO> custosMotorista;
    private List<CustoPedagioDTO> custosPedagio;
    private List<CustoPatinsDTO> custosPatins;
    private List<CustoHoraParadaDTO> custosHoraParada;
    private List<CustoHoraTrabalhadaDTO> custosHoraTrabalhada;
    private List<CustoDiariaDTO> custosDiaria;
    private List<CustoRodaExtraDTO> custosRodaExtra;
    private List<CustoExcedenteDTO> custosExcedente;

    private BigDecimal totalFinal; // soma de todos os custos
}
