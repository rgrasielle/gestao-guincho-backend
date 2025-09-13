package com.sistemaguincho.gestaoguincho.entity.custo;

import com.sistemaguincho.gestaoguincho.entity.Financeiro;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "custo_patins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustoPatins {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "financeiro_id", nullable = false)
    private Financeiro financeiro;
}
