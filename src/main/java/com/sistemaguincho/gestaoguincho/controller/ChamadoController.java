package com.sistemaguincho.gestaoguincho.controller;

import com.sistemaguincho.gestaoguincho.dto.ChamadoRequestDTO;
import com.sistemaguincho.gestaoguincho.dto.ChamadoResponseDTO;
import com.sistemaguincho.gestaoguincho.dto.ChamadoStatusUpdateDTO;
import com.sistemaguincho.gestaoguincho.enums.Status;
import com.sistemaguincho.gestaoguincho.service.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

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

        System.out.println("DTO recebido para criar chamado: " + dto);

        ChamadoResponseDTO criado = service.criarChamado(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping
    public ResponseEntity<Page<ChamadoResponseDTO>> listar(


            @RequestParam(required = false) String busca,

            @RequestParam(required = false) Long motoristaId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tipoServico,
            @RequestParam(required = false) String seguradora,

            @RequestParam(required = false) LocalDate dataServicoInicio,
            @RequestParam(required = false) LocalDate dataServicoFim,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dataAbertura"));

        // A lógica de conversão do status para Enum continua a mesma, está correta.
        Status statusEnum = null;
        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("todos")) {
            try {
                statusEnum = Status.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Ignora status inválido
            }
        }

        // CHAMADA DE SERVIÇO
        Page<ChamadoResponseDTO> chamados = service.listarChamados(
                busca,          // <-- novo
                statusEnum,
                tipoServico,
                seguradora,
                motoristaId,    // <-- novo
                dataServicoInicio,
                dataServicoFim,
                pageable
        );

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

    // Atualizar apenas o status
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

    // Endpoint para obter uma lista de seguradoras únicas.
    @GetMapping("/seguradoras")
    public ResponseEntity<List<String>> getSeguradoras() {
        List<String> seguradoras = service.listarSeguradorasDistintas();
        return ResponseEntity.ok(seguradoras);
    }

    // Endpoint para obter uma lista de tipos de serviço únicos.
    @GetMapping("/tipos-servico")
    public ResponseEntity<List<String>> getTiposServico() {
        List<String> tiposServico = service.listarTiposServicoDistintos();
        return ResponseEntity.ok(tiposServico);
    }
}

