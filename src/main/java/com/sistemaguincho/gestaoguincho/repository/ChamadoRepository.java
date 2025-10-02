package com.sistemaguincho.gestaoguincho.repository;

import com.sistemaguincho.gestaoguincho.entity.Chamado;
import com.sistemaguincho.gestaoguincho.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long>, JpaSpecificationExecutor<Chamado> {

    // Busca as seguradoras distintas a partir da entidade Chamado, acessando o campo 'seguradora'.
    @Query("SELECT DISTINCT c.seguradora FROM Chamado c WHERE c.seguradora IS NOT NULL AND c.seguradora != '' ORDER BY c.seguradora ASC")
    List<String> findDistinctSeguradoras();

    // Busca os tipos de serviço distintos a partir da entidade Chamado, acessando o campo 'tipoServico'.
    @Query("SELECT DISTINCT c.tipoServico FROM Chamado c WHERE c.tipoServico IS NOT NULL AND c.tipoServico != '' ORDER BY c.tipoServico ASC")
    List<String> findDistinctTiposServico();

    // Verifica se existe algum chamado ABERTO para um motorista específico
    boolean existsByMotoristaIdAndStatus(Long motoristaId, Status status);

    // Verifica se existe algum chamado ABERTO para um guincho específico
    boolean existsByGuinchoIdAndStatus(Long guinchoId, Status status);
}
