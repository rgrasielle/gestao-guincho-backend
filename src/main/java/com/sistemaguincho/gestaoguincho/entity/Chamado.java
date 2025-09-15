package com.sistemaguincho.gestaoguincho.entity;

import com.sistemaguincho.gestaoguincho.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "chamado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dados do serviço
    private String seguradora;
    private String sinistro;
    private LocalDate dataServico;
    private LocalTime hora;
    private String tipoServico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guincho_id")
    private Guincho guincho;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motorista_id")
    private Motorista motorista;

    // Origem
    private String origemCep;
    private String origemCidade;
    private String origemEstado;
    private String origemBairro;
    private String origemLogradouro;
    private String origemNumero;

    // Destino
    private String destinoCep;
    private String destinoCidade;
    private String destinoEstado;
    private String destinoBairro;
    private String destinoLogradouro;
    private String destinoNumero;

    // Dados do veículo
    private String veiculoModelo;
    private Integer veiculoAno;
    private String veiculoCor;
    private String veiculoPlaca;
    @Column(columnDefinition = "TEXT")
    private String veiculoObservacoes;

    // Dados do cliente
    private String clienteNome;
    private String clienteCpfCnpj;
    private String clienteTelefone;
    private String clienteEmail;
    private String clienteSolicitante;

    // Observações gerais
    @Column(columnDefinition = "TEXT")
    private String observacoes;

    // Status, datas e timestamps

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private OffsetDateTime dataAbertura;
    private OffsetDateTime dataFechamento;

    @CreationTimestamp
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    private OffsetDateTime updatedAt;
}
