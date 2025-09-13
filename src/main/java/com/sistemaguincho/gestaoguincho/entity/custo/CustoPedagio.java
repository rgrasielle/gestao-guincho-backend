package com.sistemaguincho.gestaoguincho.entity.custo;

import com.sistemaguincho.gestaoguincho.entity.Financeiro;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "custo_pedagio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustoPedagio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantidade;
    private BigDecimal valor;
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "financeiro_id", nullable = false)
    private Financeiro financeiro;

    public void calcularTotal() {
        BigDecimal vlr = this.valor != null ? this.valor : BigDecimal.ZERO;
        Integer qtd = this.quantidade != null ? this.quantidade : 0;

        this.total = vlr.multiply(new BigDecimal(qtd));
    }
}
