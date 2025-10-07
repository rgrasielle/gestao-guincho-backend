package com.sistemaguincho.gestaoguincho.entity;

import com.sistemaguincho.gestaoguincho.entity.custo.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private List<CustoQuilometragem> custosQuilometragem = new ArrayList<>();

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustoMotorista> custosMotorista = new ArrayList<>();

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustoPedagio> custosPedagio = new ArrayList<>();

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustoPatins> custosPatins = new ArrayList<>();

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustoHoraParada> custosHoraParada = new ArrayList<>();

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustoHoraTrabalhada> custosHoraTrabalhada = new ArrayList<>();

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustoDiaria> custosDiaria = new ArrayList<>();

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustoRodaExtra> custosRodaExtra = new ArrayList<>();

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustoExcedente> custosExcedente = new ArrayList<>();

    // Campos financeiros
    @Column(name = "subtotal", nullable = false)
    private BigDecimal subtotal;

    @Column(name = "desconto", nullable = false)
    private BigDecimal desconto = BigDecimal.ZERO;

    @Column(name = "total", nullable = false)
    private BigDecimal total;


    public void calcularTotais() {
        // Passo 1: Calcular o subtotal somando todos os custos
        BigDecimal subtotalCalculado = BigDecimal.ZERO;

        if (custosQuilometragem != null) {
            for (CustoQuilometragem c : custosQuilometragem) {
                if (c.getTotal() != null) subtotalCalculado = subtotalCalculado.add(c.getTotal());
            }
        }

        if (custosMotorista != null) {
            for (CustoMotorista c : custosMotorista) {
                if (c.getTotal() != null) subtotalCalculado = subtotalCalculado.add(c.getTotal());
            }
        }

        if (custosPedagio != null) {
            for (CustoPedagio c : custosPedagio) {
                if (c.getTotal() != null) subtotalCalculado = subtotalCalculado.add(c.getTotal());
            }
        }

        if (custosPatins != null) {
            for (CustoPatins c : custosPatins) {
                if (c.getValor() != null) subtotalCalculado = subtotalCalculado.add(c.getValor());
            }
        }

        if (custosHoraParada != null) {
            for (CustoHoraParada c : custosHoraParada) {
                if (c.getValorTotal() != null) subtotalCalculado = subtotalCalculado.add(c.getValorTotal());
            }
        }

        if (custosHoraTrabalhada != null) {
            for (CustoHoraTrabalhada c : custosHoraTrabalhada) {
                if (c.getValorTotal() != null) subtotalCalculado = subtotalCalculado.add(c.getValorTotal());
            }
        }

        if (custosDiaria != null) {
            for (CustoDiaria c : custosDiaria) {
                if (c.getValorTotal() != null) subtotalCalculado = subtotalCalculado.add(c.getValorTotal());
            }
        }

        if (custosRodaExtra != null) {
            for (CustoRodaExtra c : custosRodaExtra) {
                if (c.getValor() != null) subtotalCalculado = subtotalCalculado.add(c.getValor());
            }
        }

        if (custosExcedente != null) {
            for (CustoExcedente c : custosExcedente) {
                if (c.getExcedentes() != null) subtotalCalculado = subtotalCalculado.add(c.getExcedentes());
            }
        }

        // Passo 2: Atribuir o valor calculado ao campo 'subtotal'
        this.subtotal = subtotalCalculado;

        // Passo 3: Garantir que o desconto não seja nulo para o cálculo
        BigDecimal descontoAtual = (this.desconto != null) ? this.desconto : BigDecimal.ZERO;

        // Passo 4: Calcular o total final e atribuir ao campo 'total'
        this.total = this.subtotal.subtract(descontoAtual);
    }
}
