package com.sistemaguincho.gestaoguincho.entity.custo;

import com.sistemaguincho.gestaoguincho.entity.Financeiro;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "custo_quilometragem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustoQuilometragem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal quilometrosRodados;
    private BigDecimal valorPorKm;
    private BigDecimal kmSaida;
    private BigDecimal valorSaida;
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "financeiro_id", nullable = false)
    private Financeiro financeiro;
}
