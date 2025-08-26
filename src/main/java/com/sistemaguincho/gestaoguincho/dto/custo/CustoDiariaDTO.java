package com.sistemaguincho.gestaoguincho.dto.custo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustoDiariaDTO {

    private String sinistro;
    private LocalDate entrada;
    private LocalDate saida;
    private Integer estadia;
    private BigDecimal valorPorDia;
    private BigDecimal valorTotal;
}
