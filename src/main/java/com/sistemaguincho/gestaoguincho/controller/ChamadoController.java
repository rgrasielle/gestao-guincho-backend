package com.sistemaguincho.gestaoguincho.controller;

import com.sistemaguincho.gestaoguincho.dto.ChamadoRequestDTO;
import com.sistemaguincho.gestaoguincho.dto.ChamadoResponseDTO;
import com.sistemaguincho.gestaoguincho.dto.ChamadoStatusUpdateDTO;
import com.sistemaguincho.gestaoguincho.entity.Chamado;
import com.sistemaguincho.gestaoguincho.service.ChamadoService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chamados")
public class ChamadoController {

    private final ChamadoService service;

    public ChamadoController(ChamadoService service) {
        this.service = service;
    }

    // Criar chamado
    @PostMapping
    public ResponseEntity<ChamadoResponseDTO> criar(@Valid @RequestBody ChamadoRequestDTO dto) {
        ChamadoResponseDTO criado = service.criarChamado(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    // Listar chamados (com filtros opcionais e paginação)
    @GetMapping
    public ResponseEntity<Page<ChamadoResponseDTO>> listar(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String placa,
            @RequestParam(required = false) String documento,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChamadoResponseDTO> chamados = service.listarChamados(status, placa, documento, pageable);
        return ResponseEntity.ok(chamados);
    }

    // Buscar chamado por ID
    @GetMapping("/{id}")
    public ResponseEntity<ChamadoResponseDTO> buscarPorId(@PathVariable Long id) {
        ChamadoResponseDTO chamado = service.buscarPorId(id);
        return ResponseEntity.ok(chamado);
    }

    // Atualizar chamado (todos os dados)
    @PutMapping("/{id}")
    public ResponseEntity<ChamadoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ChamadoRequestDTO dto
    ) {
        ChamadoResponseDTO atualizado = service.atualizarChamado(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    // Atualizar status do chamado
    @PatchMapping("/{id}/status")
    public ResponseEntity<ChamadoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody ChamadoStatusUpdateDTO dto
    ) {
        ChamadoResponseDTO atualizado = service.atualizarStatus(id, dto.getStatus());
        return ResponseEntity.ok(atualizado);
    }

    // Deletar chamado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarChamado(id);
        return ResponseEntity.noContent().build();
    }

}
