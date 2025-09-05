package com.sistemaguincho.gestaoguincho.repository;

import com.sistemaguincho.gestaoguincho.entity.ValoresFixos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValoresFixosRepository extends JpaRepository<ValoresFixos, Long> {

}
