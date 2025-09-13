package com.sistemaguincho.gestaoguincho.controller;

import com.sistemaguincho.gestaoguincho.dto.GuinchoDisponibilidadeUpdateDTO;
import com.sistemaguincho.gestaoguincho.dto.GuinchoRequestDTO;
import com.sistemaguincho.gestaoguincho.dto.GuinchoResponseDTO;
import com.sistemaguincho.gestaoguincho.enums.Disponibilidade;
import com.sistemaguincho.gestaoguincho.service.GuinchoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guinchos")
public class GuinchoController {

    private final GuinchoService service;

    public GuinchoController(GuinchoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<GuinchoResponseDTO> criar(@RequestBody GuinchoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @GetMapping
    public List<GuinchoResponseDTO> listar() {
        return service.listar();
    }

    @PutMapping("/{id}")
    public GuinchoResponseDTO atualizar(@PathVariable Long id, @RequestBody GuinchoRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<GuinchoResponseDTO> atualizarDisponibilidade(
            @PathVariable Long id,
            @Valid @RequestBody GuinchoDisponibilidadeUpdateDTO dto) {
        GuinchoResponseDTO atualizado = service.atualizarDisponibilidade(id, dto.getDisponibilidade());
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
