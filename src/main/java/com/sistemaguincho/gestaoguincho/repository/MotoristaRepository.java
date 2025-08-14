package com.sistemaguincho.gestaoguincho.repository;

import com.sistemaguincho.gestaoguincho.entity.Motorista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotoristaRepository extends JpaRepository<Motorista, Long> {

}
