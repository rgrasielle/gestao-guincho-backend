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
public class CustoHoraParadaDTO {

    private BigDecimal horas;
    private BigDecimal valorHoraParada;
    private BigDecimal valorTotal;

}
