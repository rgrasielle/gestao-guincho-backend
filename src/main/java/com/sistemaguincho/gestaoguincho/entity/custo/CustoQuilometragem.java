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
    private BigDecimal quantidadeSaida;
    private BigDecimal valorSaida;
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "financeiro_id", nullable = false)
    private Financeiro financeiro;

    public void calcularTotal() {
        // Garante que valores nulos sejam tratados como zero para o cálculo
        BigDecimal kmRodados = this.quilometrosRodados != null ? this.quilometrosRodados : BigDecimal.ZERO;
        BigDecimal valorKm = this.valorPorKm != null ? this.valorPorKm : BigDecimal.ZERO;
        BigDecimal qtdSaida = this.quantidadeSaida != null ? this.quantidadeSaida : BigDecimal.ZERO;
        BigDecimal vSaida = this.valorSaida != null ? this.valorSaida : BigDecimal.ZERO;

        // Calcula as duas partes separadamente para clareza
        BigDecimal custoPorKm = kmRodados.multiply(valorKm);
        BigDecimal custoSaida = qtdSaida.multiply(vSaida);

        // Soma as duas partes para obter o total final
        this.total = custoPorKm.add(custoSaida);
    }
}
