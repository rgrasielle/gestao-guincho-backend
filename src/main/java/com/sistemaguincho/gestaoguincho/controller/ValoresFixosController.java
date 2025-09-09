package com.sistemaguincho.gestaoguincho.controller;

import com.sistemaguincho.gestaoguincho.dto.ValoresFixosRequest;
import com.sistemaguincho.gestaoguincho.dto.ValoresFixosResponse;
import com.sistemaguincho.gestaoguincho.service.ValoresFixosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/valores-fixos")
public class ValoresFixosController {

    private final ValoresFixosService service;

    public ValoresFixosController(ValoresFixosService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ValoresFixosResponse> getValores() {
        ValoresFixosResponse response = service.getValores();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ValoresFixosResponse> atualizar(@RequestBody ValoresFixosRequest request) {
        ValoresFixosResponse response = service.salvarOuAtualizar(request);
        return ResponseEntity.ok(response);
    }
}

