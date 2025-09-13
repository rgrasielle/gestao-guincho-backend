package com.sistemaguincho.gestaoguincho.controller;

import com.sistemaguincho.gestaoguincho.dto.MotoristaDisponibilidadeUpdateDTO;
import com.sistemaguincho.gestaoguincho.dto.MotoristaRequestDTO;
import com.sistemaguincho.gestaoguincho.dto.MotoristaResponseDTO;
import com.sistemaguincho.gestaoguincho.enums.Disponibilidade;
import com.sistemaguincho.gestaoguincho.service.MotoristaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/motoristas")
public class MotoristaController {
    private final MotoristaService service;

    public MotoristaController(MotoristaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MotoristaResponseDTO> criar(@Valid @RequestBody MotoristaRequestDTO dto) {
        return ResponseEntity.ok(service.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<MotoristaResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotoristaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MotoristaRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<MotoristaResponseDTO> atualizarDisponibilidade(
            @PathVariable Long id,
            @Valid @RequestBody MotoristaDisponibilidadeUpdateDTO dto
    ) {
        MotoristaResponseDTO atualizado = service.atualizarDisponibilidade(id, dto.getDisponibilidade());
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
