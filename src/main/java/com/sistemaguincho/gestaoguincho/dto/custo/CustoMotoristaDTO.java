package com.sistemaguincho.gestaoguincho.dto.custo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustoMotoristaDTO {

    private BigDecimal quilometrosRodados;
    private BigDecimal valorPorKm;
    private BigDecimal kmSaida;
    private BigDecimal valorSaida;
    private BigDecimal total;
}
