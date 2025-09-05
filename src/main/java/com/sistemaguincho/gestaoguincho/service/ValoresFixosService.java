package com.sistemaguincho.gestaoguincho.service;

import com.sistemaguincho.gestaoguincho.dto.ValoresFixosRequest;
import com.sistemaguincho.gestaoguincho.dto.ValoresFixosResponse;
import com.sistemaguincho.gestaoguincho.entity.ValoresFixos;
import com.sistemaguincho.gestaoguincho.repository.ValoresFixosRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValoresFixosService {

    private final ValoresFixosRepository repository;

    public ValoresFixosService(ValoresFixosRepository repository) {
        this.repository = repository;
    }

    public ValoresFixosResponse getValores() {
        Optional<ValoresFixos> valores = repository.findAll().stream().findFirst();
        return valores.map(v -> new ValoresFixosResponse(
                v.getValorQuilometragemPorKm(),
                v.getValorQuilometragemSaida(),
                v.getValorMotoristaPorKm(),
                v.getValorMotoristaSaida(),
                v.getValorHoraParada(),
                v.getValorHoraTrabalhada(),
                v.getValorDiaria()
        )).orElse(null);
    }

    public ValoresFixosResponse salvarOuAtualizar(ValoresFixosRequest request) {
        ValoresFixos valores = repository.findAll().stream().findFirst().orElse(new ValoresFixos());

        valores.setValorQuilometragemPorKm(request.getValorQuilometragemPorKm());
        valores.setValorQuilometragemSaida(request.getValorQuilometragemSaida());
        valores.setValorMotoristaPorKm(request.getValorMotoristaPorKm());
        valores.setValorMotoristaSaida(request.getValorMotoristaSaida());
        valores.setValorHoraParada(request.getValorHoraParada());
        valores.setValorHoraTrabalhada(request.getValorHoraTrabalhada());
        valores.setValorDiaria(request.getValorDiaria());

        repository.save(valores);

        return new ValoresFixosResponse(
                valores.getValorQuilometragemPorKm(),
                valores.getValorQuilometragemSaida(),
                valores.getValorMotoristaPorKm(),
                valores.getValorMotoristaSaida(),
                valores.getValorHoraParada(),
                valores.getValorHoraTrabalhada(),
                valores.getValorDiaria()
        );
    }
}
