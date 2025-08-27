package com.sistemaguincho.gestaoguincho.entity;

import com.sistemaguincho.gestaoguincho.entity.custo.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "financeiro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Financeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento com Chamado (1 Financeiro → 1 Chamado)
    @OneToOne
    @JoinColumn(name = "chamado_id", nullable = false, unique = true)
    private Chamado chamado;

    // Relacionamentos com as entidades filhas de custos (1 Financeiro → N custos)
    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustoQuilometragem> custosQuilometragem;

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustoMotorista> custosMotorista;

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustoPedagio> custosPedagio;

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustoPatins> custosPatins;

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustoHoraParada> custosHoraParada;

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustoHoraTrabalhada> custosHoraTrabalhada;

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustoDiaria> custosDiaria;

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustoRodaExtra> custosRodaExtra;

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustoExcedente> custosExcedente;


    @Column(name = "total")
    private BigDecimal totalFinal; // Valor final calculado automaticamente


    // Método para calcular o total final
    public void calcularTotalFinal() {
        BigDecimal total = BigDecimal.ZERO;

        if (custosQuilometragem != null) {
            for (CustoQuilometragem c : custosQuilometragem) {
                if (c.getTotal() != null) total = total.add(c.getTotal());
            }
        }

        if (custosMotorista != null) {
            for (CustoMotorista c : custosMotorista) {
                if (c.getTotal() != null) total = total.add(c.getTotal());
            }
        }

        if (custosPedagio != null) {
            for (CustoPedagio c : custosPedagio) {
                if (c.getTotal() != null) total = total.add(c.getTotal());
            }
        }

        if (custosPatins != null) {
            for (CustoPatins c : custosPatins) {
                if (c.getValor() != null) total = total.add(c.getValor());
            }
        }

        if (custosHoraParada != null) {
            for (CustoHoraParada c : custosHoraParada) {
                if (c.getValorTotal() != null) total = total.add(c.getValorTotal());
            }
        }

        if (custosHoraTrabalhada != null) {
            for (CustoHoraTrabalhada c : custosHoraTrabalhada) {
                if (c.getValorTotal() != null) total = total.add(c.getValorTotal());
            }
        }

        if (custosDiaria != null) {
            for (CustoDiaria c : custosDiaria) {
                if (c.getValorTotal() != null) total = total.add(c.getValorTotal());
            }
        }

        if (custosRodaExtra != null) {
            for (CustoRodaExtra c : custosRodaExtra) {
                if (c.getValor() != null) total = total.add(c.getValor());
            }
        }

        if (custosExcedente != null) {
            for (CustoExcedente c : custosExcedente) {
                if (c.getExcedentes() != null) total = total.add(c.getExcedentes());
            }
        }

        this.totalFinal = total;
    }
}
