package com.sistemaguincho.gestaoguincho.repository;

import com.sistemaguincho.gestaoguincho.entity.Financeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FinanceiroRepository extends JpaRepository<Financeiro, Long> {

    Optional<Financeiro> findByChamadoId(Long chamadoId);

}
