package com.sistemaguincho.gestaoguincho.entity.custo;

import com.sistemaguincho.gestaoguincho.entity.Financeiro;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "custo_diaria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustoDiaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sinistro;
    private LocalDate entrada;
    private LocalDate saida;
    private BigDecimal estadia;
    private BigDecimal valorPorDia;
    private BigDecimal valorTotal;

    @ManyToOne
    @JoinColumn(name = "financeiro_id", nullable = false)
    private Financeiro financeiro;
}
