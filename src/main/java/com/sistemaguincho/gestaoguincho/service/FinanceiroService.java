package com.sistemaguincho.gestaoguincho.service;

import com.sistemaguincho.gestaoguincho.dto.FinanceiroDTO;
import com.sistemaguincho.gestaoguincho.dto.FinanceiroRequestDTO;
import com.sistemaguincho.gestaoguincho.dto.FinanceiroResponseDTO;
import com.sistemaguincho.gestaoguincho.dto.custo.*;
import com.sistemaguincho.gestaoguincho.entity.Chamado;
import com.sistemaguincho.gestaoguincho.entity.Financeiro;
import com.sistemaguincho.gestaoguincho.entity.custo.*;
import com.sistemaguincho.gestaoguincho.repository.ChamadoRepository;
import com.sistemaguincho.gestaoguincho.repository.FinanceiroRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinanceiroService {

    private final FinanceiroRepository financeiroRepository;
    private final ChamadoRepository chamadoRepository;

    public FinanceiroService(FinanceiroRepository financeiroRepository,
                             ChamadoRepository chamadoRepository) {
        this.financeiroRepository = financeiroRepository;
        this.chamadoRepository = chamadoRepository;
    }

    // Criar/atualizar financeiro vinculado a um chamado
    @Transactional
    public FinanceiroDTO salvarFinanceiro(Long chamadoId, FinanceiroDTO dto) {
        // 1 Busca o chamado pelo ID
        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new EntityNotFoundException("Chamado não encontrado"));

        // 2 Busca o financeiro existente ou cria um novo
        Financeiro financeiro = financeiroRepository.findByChamadoId(chamadoId)
                .orElse(new Financeiro());

        financeiro.setChamado(chamado);

        // 3 Limpa listas antigas (se existirem)
        if (financeiro.getId() != null) {
            financeiro.getCustosQuilometragem().clear();
            financeiro.getCustosMotorista().clear();
            financeiro.getCustosPedagio().clear();
            financeiro.getCustosPatins().clear();
            financeiro.getCustosHoraParada().clear();
            financeiro.getCustosHoraTrabalhada().clear();
            financeiro.getCustosDiaria().clear();
            financeiro.getCustosRodaExtra().clear();
            financeiro.getCustosExcedente().clear();
        }

        // 4 Constrói cada tipo de custo e adiciona à lista existente

        // --- Pagamento Quilometragem ---
        CustoQuilometragem custoPgto = new CustoQuilometragem();
        custoPgto.setQuilometrosRodados(dto.getPagamentoKmRodados());
        custoPgto.setValorPorKm(dto.getPagamentoValorKm());
        custoPgto.setQuantidadeSaida(dto.getPagamentoQuantidadeSaida());
        custoPgto.setValorSaida(dto.getPagamentoValorSaida());
        custoPgto.calcularTotal();
        custoPgto.setFinanceiro(financeiro);
        financeiro.getCustosQuilometragem().add(custoPgto);

        // --- Acerto com Motorista ---
        CustoMotorista custoAcerto = new CustoMotorista();
        custoAcerto.setQuilometrosRodados(dto.getAcertoKmRodados());
        custoAcerto.setValorPorKm(dto.getAcertoValorKm());
        custoAcerto.setQuantidadeSaida(dto.getAcertoQuantidadeSaida());
        custoAcerto.setValorSaida(dto.getAcertoValorSaida());
        custoAcerto.calcularTotal();
        custoAcerto.setFinanceiro(financeiro);
        financeiro.getCustosMotorista().add(custoAcerto);

        // --- Pedágios ---
        if (dto.getCustosPedagio() != null) {
            for (FinanceiroDTO.CustoPedagioDTO p : dto.getCustosPedagio()) {
                CustoPedagio c = new CustoPedagio();
                c.setQuantidade(p.getQuantidadePedagio());
                c.setValor(p.getValorPedagio());
                c.calcularTotal();
                c.setFinanceiro(financeiro);
                financeiro.getCustosPedagio().add(c);
            }
        }

        // --- Patins ---
        CustoPatins custoPatins = new CustoPatins();
        custoPatins.setDescricao(dto.getDescricaoPatins());
        custoPatins.setValor(dto.getValorPatins());
        custoPatins.setFinanceiro(financeiro);
        financeiro.getCustosPatins().add(custoPatins);

        // --- Hora Parada ---
        CustoHoraParada custoHoraParada = new CustoHoraParada();
        custoHoraParada.setHoras(dto.getHorasParadas());
        custoHoraParada.setValorHoraParada(dto.getValorHoraParada());
        custoHoraParada.calcularTotal();
        custoHoraParada.setFinanceiro(financeiro);
        financeiro.getCustosHoraParada().add(custoHoraParada);

        // --- Hora Trabalhada ---
        CustoHoraTrabalhada custoHoraTrabalhada = new CustoHoraTrabalhada();
        custoHoraTrabalhada.setHoras(dto.getHorasTrabalhadas());
        custoHoraTrabalhada.setValorHoraTrabalhada(dto.getValorHoraTrabalhada());
        custoHoraTrabalhada.calcularTotal();
        custoHoraTrabalhada.setFinanceiro(financeiro);
        financeiro.getCustosHoraTrabalhada().add(custoHoraTrabalhada);

        // --- Diárias ---
        CustoDiaria custoDiaria = new CustoDiaria();
        custoDiaria.setEntrada(dto.getEntradaDiarias());
        custoDiaria.setSaida(dto.getSaidaDiarias());
        custoDiaria.setEstadia(dto.getEstadiasDiarias());
        custoDiaria.setValorPorDia(dto.getValorDiaria());
        custoDiaria.calcularTotal();
        custoDiaria.setFinanceiro(financeiro);
        financeiro.getCustosDiaria().add(custoDiaria);

        // --- Rodas Extras ---
        CustoRodaExtra custoRodaExtra = new CustoRodaExtra();
        custoRodaExtra.setDescricao(dto.getDescricaoRodas());
        custoRodaExtra.setValor(dto.getValorRodas());
        custoRodaExtra.setFinanceiro(financeiro);
        financeiro.getCustosRodaExtra().add(custoRodaExtra);

        // --- Excedentes ---
        CustoExcedente custoExcedente = new CustoExcedente();
        custoExcedente.setExcedentes(dto.getExcedente());
        custoExcedente.setObservacao(dto.getObservacoesExcedente());
        custoExcedente.setFinanceiro(financeiro);
        financeiro.getCustosExcedente().add(custoExcedente);

        // 5 Define o desconto na entidade ANTES de calcular
        financeiro.setDesconto(dto.getDesconto());

        // 6 Recalcula o subtotal e o total final com base nos custos e no desconto
        financeiro.calcularTotais();

        // 7 Salva e retorna o DTO atualizado
        Financeiro salvo = financeiroRepository.save(financeiro);
        return convertToDto(salvo);
    }

    // Buscar financeiro de um chamado específico
    public FinanceiroDTO buscarPorChamado(Long chamadoId) {
        Financeiro financeiro = financeiroRepository.findByChamadoId(chamadoId)
                .orElseThrow(() -> new EntityNotFoundException("Financeiro não encontrado para este chamado"));
        return convertToDto(financeiro);
    }

    // NOVO: Método privado para "achatar" a Entidade em um DTO
    private FinanceiroDTO convertToDto(Financeiro financeiro) {
        if (financeiro == null) {
            return null;
        }

        FinanceiroDTO dto = new FinanceiroDTO();
        dto.setId(financeiro.getId());

        // Mapeia os campos para o DTO
        dto.setSubtotal(financeiro.getSubtotal());
        dto.setDesconto(financeiro.getDesconto());
        dto.setTotal(financeiro.getTotal());

        // Extrai os dados da lista de CustoQuilometragem (geralmente terá 1 item)
        if (financeiro.getCustosQuilometragem() != null && !financeiro.getCustosQuilometragem().isEmpty()) {
            CustoQuilometragem custo = financeiro.getCustosQuilometragem().get(0);
            dto.setPagamentoKmRodados(custo.getQuilometrosRodados());
            dto.setPagamentoValorKm(custo.getValorPorKm());
            dto.setPagamentoQuantidadeSaida(custo.getQuantidadeSaida());
            dto.setPagamentoValorSaida(custo.getValorSaida());
            dto.setTotalPagamento(custo.getTotal());
        }

        // Extrai os dados da lista de CustoMotorista
        if (financeiro.getCustosMotorista() != null && !financeiro.getCustosMotorista().isEmpty()) {
            CustoMotorista custo = financeiro.getCustosMotorista().get(0);
            dto.setAcertoKmRodados(custo.getQuilometrosRodados());
            dto.setAcertoValorKm(custo.getValorPorKm());
            dto.setAcertoQuantidadeSaida(custo.getQuantidadeSaida());
            dto.setAcertoValorSaida(custo.getValorSaida());
            dto.setTotalAcerto(custo.getTotal());
        }

        // Extrai os dados da lista de CustoPedagios
        if (financeiro.getCustosPedagio() != null && !financeiro.getCustosPedagio().isEmpty()) {
            List<FinanceiroDTO.CustoPedagioDTO> listaPedagiosDto = financeiro.getCustosPedagio().stream()
                    .map(custo -> new FinanceiroDTO.CustoPedagioDTO(custo.getQuantidade(), custo.getValor()))
                    .collect(Collectors.toList());
            dto.setCustosPedagio(listaPedagiosDto);
        }

        // Extrai os dados da lista de CustoPatins
        if (financeiro.getCustosPatins() != null && !financeiro.getCustosPatins().isEmpty()) {
            CustoPatins custo = financeiro.getCustosPatins().get(0);
            dto.setDescricaoPatins(custo.getDescricao());
            dto.setValorPatins(custo.getValor());
        }

        // Extrai os dados da lista de CustoHoraParada
        if (financeiro.getCustosHoraParada() != null && !financeiro.getCustosHoraParada().isEmpty()) {
            CustoHoraParada custo = financeiro.getCustosHoraParada().get(0);
            dto.setHorasParadas(custo.getHoras());
            dto.setValorHoraParada(custo.getValorHoraParada());
            dto.setTotalHoraParada(custo.getValorTotal());
        }

        // Extrai os dados da lista de CustoHoraTrabalhada
        if (financeiro.getCustosHoraTrabalhada() != null && !financeiro.getCustosHoraTrabalhada().isEmpty()) {
            CustoHoraTrabalhada custo = financeiro.getCustosHoraTrabalhada().get(0);
            dto.setHorasTrabalhadas(custo.getHoras());
            dto.setValorHoraTrabalhada(custo.getValorHoraTrabalhada());
            dto.setTotalHoraTrabalhada(custo.getValorTotal());
        }

        // Extrai os dados da lista de CustoDiarias
        if (financeiro.getCustosDiaria() != null && !financeiro.getCustosDiaria().isEmpty()) {
            CustoDiaria custo = financeiro.getCustosDiaria().get(0);
            dto.setEntradaDiarias(custo.getEntrada());
            dto.setSaidaDiarias(custo.getSaida());
            dto.setEstadiasDiarias(custo.getEstadia());
            dto.setValorDiaria(custo.getValorPorDia());
            dto.setTotalDiarias(custo.getValorTotal());
        }

        // Extrai os dados da lista de CustoRodaExtra
        if (financeiro.getCustosRodaExtra() != null && !financeiro.getCustosRodaExtra().isEmpty()) {
            CustoRodaExtra custo = financeiro.getCustosRodaExtra().get(0);
            dto.setDescricaoRodas(custo.getDescricao());
            dto.setValorRodas(custo.getValor());
        }

        // Extrai os dados da lista de CustoExcedente
        if (financeiro.getCustosExcedente() != null && !financeiro.getCustosExcedente().isEmpty()) {
            CustoExcedente custo = financeiro.getCustosExcedente().get(0);
            dto.setExcedente(custo.getExcedentes());
            dto.setObservacoesExcedente(custo.getObservacao());
        }

        return dto;
    }
}

