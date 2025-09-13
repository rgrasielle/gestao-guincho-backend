package com.sistemaguincho.gestaoguincho.controller;

import com.sistemaguincho.gestaoguincho.dto.FinanceiroDTO;
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
    public ResponseEntity<FinanceiroDTO> salvarFinanceiro(
            @PathVariable Long chamadoId,
            @RequestBody FinanceiroDTO dto) {

        FinanceiroDTO response = financeiroService.salvarFinanceiro(chamadoId, dto);
        return ResponseEntity.ok(response);
    }

    // Buscar o financeiro de um chamado específico
    @GetMapping
    public ResponseEntity<FinanceiroDTO> buscarFinanceiro(@PathVariable Long chamadoId) {
        FinanceiroDTO response = financeiroService.buscarPorChamado(chamadoId);
        return ResponseEntity.ok(response);
    }
}