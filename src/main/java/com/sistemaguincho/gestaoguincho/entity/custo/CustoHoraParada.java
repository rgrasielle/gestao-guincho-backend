package com.sistemaguincho.gestaoguincho.entity.custo;

import com.sistemaguincho.gestaoguincho.entity.Financeiro;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "custo_hora_parada")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustoHoraParada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal horas;
    private BigDecimal valorHoraParada;
    private BigDecimal valorTotal;

    @ManyToOne
    @JoinColumn(name = "financeiro_id", nullable = false)
    private Financeiro financeiro;

    public void calcularTotal() {
        BigDecimal hrs = this.horas != null ? this.horas : BigDecimal.ZERO;
        BigDecimal vlrHora = this.valorHoraParada != null ? this.valorHoraParada : BigDecimal.ZERO;

        this.valorTotal = hrs.multiply(vlrHora);
    }
}
