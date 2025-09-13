package com.sistemaguincho.gestaoguincho.entity.custo;

import com.sistemaguincho.gestaoguincho.entity.Financeiro;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

    private LocalDate entrada;
    private LocalDate saida;
    private Integer estadia;
    private BigDecimal valorPorDia;
    private BigDecimal valorTotal;

    @ManyToOne
    @JoinColumn(name = "financeiro_id", nullable = false)
    private Financeiro financeiro;

    public void calcularTotal() {
        if (this.entrada == null || this.saida == null || this.valorPorDia == null) {
            this.estadia = 0; // O campo na sua migration é INT
            this.valorTotal = BigDecimal.ZERO;
            return;
        }

        long diasDeEstadia = ChronoUnit.DAYS.between(this.entrada, this.saida);
        this.estadia = (int) diasDeEstadia;
        this.valorTotal = this.valorPorDia.multiply(new BigDecimal(diasDeEstadia));
    }
}
