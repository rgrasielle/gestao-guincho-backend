package com.sistemaguincho.gestaoguincho.controller;

import com.sistemaguincho.gestaoguincho.dto.FinanceiroRequestDTO;
import com.sistemaguincho.gestaoguincho.dto.FinanceiroResponseDTO;
import com.sistemaguincho.gestaoguincho.service.FinanceiroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chamados/{chamadoId}/financeiro")
public class FinanceiroController {

    private final FinanceiroService financeiroService;

    public FinanceiroController(FinanceiroService financeiroService) {
        this.financeiroService = financeiroService;
    }

    // Criar ou atualizar o financeiro de um chamado
    @PostMapping
    public ResponseEntity<FinanceiroResponseDTO> salvarFinanceiro(
            @PathVariable Long chamadoId,
            @RequestBody FinanceiroRequestDTO dto) {

        FinanceiroResponseDTO response = financeiroService.salvarFinanceiro(chamadoId, dto);
        return ResponseEntity.ok(response);
    }

    // Buscar o financeiro de um chamado específico
    @GetMapping
    public ResponseEntity<FinanceiroResponseDTO> buscarFinanceiro(@PathVariable Long chamadoId) {
        FinanceiroResponseDTO response = financeiroService.buscarPorChamado(chamadoId);
        return ResponseEntity.ok(response);
    }
}
