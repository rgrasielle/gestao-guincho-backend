package com.sistemaguincho.gestaoguincho.repository;

import com.sistemaguincho.gestaoguincho.entity.Chamado;
import com.sistemaguincho.gestaoguincho.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    @Query("SELECT c FROM Chamado c " +
            "LEFT JOIN c.guincho g " +
            "LEFT JOIN c.motorista m " +
            "WHERE (:sinistro IS NULL OR c.sinistro LIKE %:sinistro%) " +
            "AND (:placa IS NULL OR g.placa LIKE %:placa%) " +
            "AND (:codigo IS NULL OR c.id = :codigo) " +
            "AND (:status IS NULL OR c.status = :status) " +
            "AND (:tipoServico IS NULL OR c.tipoServico = :tipoServico) " +
            "AND (:modeloVeiculo IS NULL OR c.veiculoModelo LIKE %:modeloVeiculo%) " +
            "AND (:seguradora IS NULL OR c.seguradora LIKE %:seguradora%) " +
            "AND (:dataAbertura IS NULL OR c.dataAbertura >= :dataAbertura) " +
            "AND (:dataFechamento IS NULL OR c.dataFechamento <= :dataFechamento)")
    Page<Chamado> buscarPorFiltros(
            @Param("sinistro") String sinistro,
            @Param("placa") String placa,
            @Param("codigo") Long codigo,
            @Param("status") Status status,
            @Param("tipoServico") String tipoServico,
            @Param("modeloVeiculo") String modeloVeiculo,
            @Param("seguradora") String seguradora,
            @Param("dataAbertura") OffsetDateTime dataAbertura,
            @Param("dataFechamento") OffsetDateTime dataFechamento,
            Pageable pageable
    );
}
