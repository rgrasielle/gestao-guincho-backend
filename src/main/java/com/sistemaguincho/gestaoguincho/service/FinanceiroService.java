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
        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new EntityNotFoundException("Chamado não encontrado"));

        Financeiro financeiro = financeiroRepository.findByChamadoId(chamadoId)
                .orElse(new Financeiro());

        financeiro.setChamado(chamado);

        // Limpa as listas antigas para garantir que os dados sejam substituídos se for uma atualização
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

        // Constrói a entidade CustoQuilometragem a partir do DTO
        CustoQuilometragem custoPgto = new CustoQuilometragem();
        custoPgto.setQuilometrosRodados(dto.getPagamentoKmRodados());
        custoPgto.setValorPorKm(dto.getPagamentoValorKm());
        custoPgto.setQuantidadeSaida(dto.getPagamentoQuantidadeSaida());
        custoPgto.setValorSaida(dto.getPagamentoValorSaida());
        custoPgto.calcularTotal();
        custoPgto.setFinanceiro(financeiro);
        financeiro.setCustosQuilometragem(List.of(custoPgto));

        // Constrói a entidade CustoMotorista a partir do DTO
        CustoMotorista custoAcerto = new CustoMotorista();
        custoAcerto.setQuilometrosRodados(dto.getAcertoKmRodados());
        custoAcerto.setValorPorKm(dto.getAcertoValorKm());
        custoAcerto.setQuantidadeSaida(dto.getAcertoQuantidadeSaida());
        custoAcerto.setValorSaida(dto.getAcertoValorSaida());
        custoAcerto.calcularTotal(); // Calcula o total
        custoAcerto.setFinanceiro(financeiro);
        financeiro.setCustosMotorista(List.of(custoAcerto));

        // Constrói a entidade CustoPedagio a partir do DTO
        if (dto.getCustosPedagio() != null) {
            List<CustoPedagio> listaCustosPedagio = dto.getCustosPedagio().stream()
                    .map(pedagioDto -> {
                        CustoPedagio custoPedagio = new CustoPedagio();
                        custoPedagio.setQuantidade(pedagioDto.getQuantidadePedagio());
                        custoPedagio.setValor(pedagioDto.getValorPedagio());
                        custoPedagio.calcularTotal();
                        custoPedagio.setFinanceiro(financeiro);
                        return custoPedagio;
                    })
                    .collect(Collectors.toList());
            financeiro.setCustosPedagio(listaCustosPedagio);
        } else {
            financeiro.setCustosPedagio(new ArrayList<>());
        }

        // Constrói a entidade CustoPatins a partir do DTO
        CustoPatins custoPatins = new CustoPatins();
        custoPatins.setDescricao(dto.getDescricaoPatins());
        custoPatins.setValor(dto.getValorPatins());
        custoPatins.setFinanceiro(financeiro);
        financeiro.setCustosPatins(List.of(custoPatins));

        // Constrói a entidade CustoHoraParada a partir do DTO
        CustoHoraParada custoHoraParada = new CustoHoraParada();
        custoHoraParada.setHoras(dto.getHorasParadas());
        custoHoraParada.setValorHoraParada(dto.getValorHoraParada());
        custoHoraParada.calcularTotal();
        custoHoraParada.setFinanceiro(financeiro);
        financeiro.setCustosHoraParada(List.of(custoHoraParada));

        // Constrói a entidade CustoHoraTrabalhada a partir do DTO
        CustoHoraTrabalhada custoHoraTrabalhada = new CustoHoraTrabalhada();
        custoHoraTrabalhada.setHoras(dto.getHorasTrabalhadas());
        custoHoraTrabalhada.setValorHoraTrabalhada(dto.getValorHoraTrabalhada());
        custoHoraTrabalhada.calcularTotal();
        custoHoraTrabalhada.setFinanceiro(financeiro);
        financeiro.setCustosHoraTrabalhada(List.of(custoHoraTrabalhada));

        // Constrói a entidade CustoDiaria a partir do DTO
        CustoDiaria custoDiaria = new CustoDiaria();
        custoDiaria.setEntrada(dto.getEntradaDiarias());
        custoDiaria.setSaida(dto.getSaidaDiarias());
        custoDiaria.setEstadia(dto.getEstadiasDiarias());
        custoDiaria.setValorPorDia(dto.getValorDiaria());
        custoDiaria.calcularTotal();
        custoDiaria.setFinanceiro(financeiro);
        financeiro.setCustosDiaria(List.of(custoDiaria));

        // Constrói a entidade CustoRodaExtra a partir do DTO
        CustoRodaExtra custoRodaExtra = new CustoRodaExtra();
        custoRodaExtra.setDescricao(dto.getDescricaoRodas());
        custoRodaExtra.setValor(dto.getValorRodas());
        custoRodaExtra.setFinanceiro(financeiro);
        financeiro.setCustosRodaExtra(List.of(custoRodaExtra));

        // Constrói a entidade CustoExcedente a partir do DTO
        CustoExcedente custoExcedente = new CustoExcedente();
        custoExcedente.setExcedentes(dto.getExcedente());
        custoExcedente.setObservacao(dto.getObservacoesExcedente());
        custoExcedente.setFinanceiro(financeiro);
        financeiro.setCustosExcedente(List.of(custoExcedente));

        // Recalcula o valor final com base nos novos custos
        financeiro.calcularTotalFinal();

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
        dto.setChamadoId(financeiro.getChamado().getId());
        dto.setValorFinal(financeiro.getTotalFinal());

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

   /* // ---------- Métodos privados para mapear e calcular cada custo ----------

    private List<CustoQuilometragem> mapCustoQuilometragem(List<CustoQuilometragemDTO> dtos, Financeiro financeiro) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            CustoQuilometragem c = modelMapper.map(dto, CustoQuilometragem.class);
            c.setTotal(c.getQuilometrosRodados().multiply(c.getValorPorKm()).add(c.getValorSaida()));
            c.setFinanceiro(financeiro);
            return c;
        }).collect(Collectors.toList());
    }

    private List<CustoMotorista> mapCustoMotorista(List<CustoMotoristaDTO> dtos, Financeiro financeiro) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            CustoMotorista c = modelMapper.map(dto, CustoMotorista.class);
            c.setTotal(c.getQuilometrosRodados().multiply(c.getValorPorKm()).add(c.getValorSaida()));
            c.setFinanceiro(financeiro);
            return c;
        }).collect(Collectors.toList());
    }

    private List<CustoPedagio> mapCustoPedagio(List<CustoPedagioDTO> dtos, Financeiro financeiro) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            CustoPedagio c = modelMapper.map(dto, CustoPedagio.class);
            c.setTotal(c.getValor().multiply(new BigDecimal(c.getQuantidade())));
            c.setFinanceiro(financeiro);
            return c;
        }).collect(Collectors.toList());
    }

    private List<CustoPatins> mapCustoPatins(List<CustoPatinsDTO> dtos, Financeiro financeiro) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            CustoPatins c = modelMapper.map(dto, CustoPatins.class);
            c.setFinanceiro(financeiro);
            return c;
        }).collect(Collectors.toList());
    }

    private List<CustoHoraParada> mapCustoHoraParada(List<CustoHoraParadaDTO> dtos, Financeiro financeiro) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            CustoHoraParada c = modelMapper.map(dto, CustoHoraParada.class);
            c.setValorTotal(c.getHoras().multiply(c.getValorHoraParada()));
            c.setFinanceiro(financeiro);
            return c;
        }).collect(Collectors.toList());
    }

    private List<CustoHoraTrabalhada> mapCustoHoraTrabalhada(List<CustoHoraTrabalhadaDTO> dtos, Financeiro financeiro) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            CustoHoraTrabalhada c = modelMapper.map(dto, CustoHoraTrabalhada.class);
            c.setValorTotal(c.getHoras().multiply(c.getValorHoraTrabalhada()));
            c.setFinanceiro(financeiro);
            return c;
        }).collect(Collectors.toList());
    }

    private List<CustoDiaria> mapCustoDiaria(List<CustoDiariaDTO> dtos, Financeiro financeiro) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            CustoDiaria c = modelMapper.map(dto, CustoDiaria.class);
            long estadia = ChronoUnit.DAYS.between(c.getEntrada(), c.getSaida());
            c.setEstadia(BigDecimal.valueOf(estadia));
            c.setValorTotal(c.getValorPorDia().multiply(new BigDecimal(estadia)));
            c.setFinanceiro(financeiro);
            return c;
        }).collect(Collectors.toList());
    }

    private List<CustoRodaExtra> mapCustoRodaExtra(List<CustoRodaExtraDTO> dtos, Financeiro financeiro) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            CustoRodaExtra c = modelMapper.map(dto, CustoRodaExtra.class);
            c.setFinanceiro(financeiro);
            return c;
        }).collect(Collectors.toList());
    }

    private List<CustoExcedente> mapCustoExcedente(List<CustoExcedenteDTO> dtos, Financeiro financeiro) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            CustoExcedente c = modelMapper.map(dto, CustoExcedente.class);
            c.setFinanceiro(financeiro);
            return c;
        }).collect(Collectors.toList());
    }
}


    */