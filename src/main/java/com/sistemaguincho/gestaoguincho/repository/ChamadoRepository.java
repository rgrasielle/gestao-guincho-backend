package com.sistemaguincho.gestaoguincho.repository;

import com.sistemaguincho.gestaoguincho.entity.Chamado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    @Query("SELECT c FROM Chamado c " +
            "WHERE (:sinistro IS NULL OR c.sinistro LIKE %:sinistro%) " +
            "AND (:placa IS NULL OR c.veiculoPlaca LIKE %:placa%) " +
            "AND (:codigo IS NULL OR c.numero LIKE %:codigo%) " +
            "AND (:status IS NULL OR c.status = :status) " +
            "AND (:tipo IS NULL OR c.tipoServico = :tipo) " +
            "AND (:veiculo IS NULL OR c.veiculoModelo LIKE %:veiculo%) " +
            "AND (:cliente IS NULL OR c.clienteNome LIKE %:cliente%) " +
            "AND (:seguradora IS NULL OR c.seguradora LIKE %:seguradora%) " +
            "AND (:entrada IS NULL OR c.dataAcionamento >= :entrada) " +
            "AND (:saida IS NULL OR c.dataAcionamento <= :saida)")
    Page<Chamado> buscarPorFiltros(
            @Param("sinistro") String sinistro,
            @Param("placa") String placa,
            @Param("codigo") String codigo,
            @Param("status") String status,
            @Param("tipo") String tipo,
            @Param("veiculo") String veiculo,
            @Param("cliente") String cliente,
            @Param("seguradora") String seguradora,
            @Param("entrada") LocalDate entrada,
            @Param("saida") LocalDate saida,
            Pageable pageable
    );
}
