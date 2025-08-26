package com.sistemaguincho.gestaoguincho.service;

import com.sistemaguincho.gestaoguincho.dto.FinanceiroRequestDTO;
import com.sistemaguincho.gestaoguincho.dto.FinanceiroResponseDTO;
import com.sistemaguincho.gestaoguincho.dto.custo.*;
import com.sistemaguincho.gestaoguincho.entity.Chamado;
import com.sistemaguincho.gestaoguincho.entity.Financeiro;
import com.sistemaguincho.gestaoguincho.entity.custo.*;
import com.sistemaguincho.gestaoguincho.repository.ChamadoRepository;
import com.sistemaguincho.gestaoguincho.repository.FinanceiroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinanceiroService {

    private final FinanceiroRepository financeiroRepository;
    private final ChamadoRepository chamadoRepository;

    public FinanceiroService(FinanceiroRepository financeiroRepository, ChamadoRepository chamadoRepository) {
        this.financeiroRepository = financeiroRepository;
        this.chamadoRepository = chamadoRepository;
    }

    public FinanceiroResponseDTO salvarFinanceiro(Long chamadoId, FinanceiroRequestDTO dto) {
        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new EntityNotFoundException("Chamado não encontrado"));

        Financeiro financeiro = new Financeiro();
        financeiro.setChamado(chamado);

        // Mapear custos e calcular totals parciais
        financeiro.setCustosQuilometragem(
                mapCustoQuilometragem(dto.getCustosQuilometragem(), financeiro)
        );

        financeiro.setCustosMotorista(
                mapCustoMotorista(dto.getCustosMotorista(), financeiro)
        );

        financeiro.setCustosPedagio(
                mapCustoPedagio(dto.getCustosPedagio(), financeiro)
        );

        financeiro.setCustosPatins(
                mapCustoPatins(dto.getCustosPatins(), financeiro)
        );

        financeiro.setCustosHoraParada(
                mapCustoHoraParada(dto.getCustosHoraParada(), financeiro)
        );

        financeiro.setCustosHoraTrabalhada(
                mapCustoHoraTrabalhada(dto.getCustosHoraTrabalhada(), financeiro)
        );

        financeiro.setCustosDiaria(
                mapCustoDiaria(dto.getCustosDiaria(), financeiro)
        );

        financeiro.setCustosRodaExtra(
                mapCustoRodaExtra(dto.getCustosRodaExtra(), financeiro)
        );

        financeiro.setCustosExcedente(
                mapCustoExcedente(dto.getCustosExcedente(), financeiro)
        );

        // Calcula total final somando todos os custos
        financeiro.calcularTotalFinal();

        financeiro = financeiroRepository.save(financeiro);
        return new FinanceiroResponseDTO(financeiro);
    }

    public FinanceiroResponseDTO buscarPorChamado(Long chamadoId) {
        Financeiro financeiro = financeiroRepository.findByChamadoId(chamadoId)
                .orElseThrow(() -> new EntityNotFoundException("Financeiro não encontrado para este chamado"));
        return new FinanceiroResponseDTO(financeiro);
    }

    // ---------- Métodos privados para mapear e calcular cada custo ----------

    private List<CustoQuilometragem> mapCustoQuilometragem(List<CustoQuilometragemDTO> dtos, Financeiro financeiro) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            CustoQuilometragem c = new CustoQuilometragem();
            c.setQuilometrosRodados(dto.getQuilometrosRodados());
            c.setValorPorKm(dto.getValorPorKm());
            c.setKmSaida(dto.getKmSaida());
            c.setValorSaida(dto.getValorSaida());
            // cálculo parcial
            c.setTotal(dto.getQuilometrosRodados().multiply(dto.getValorPorKm()).add(dto.getValorSaida()));
            c.setFinanceiro(financeiro);
            return c;
        }).collect(Collectors.toList());
    }

    private List<CustoMotorista> mapCustoMotorista(List<CustoMotoristaDTO> dtos, Financeiro financeiro) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            CustoMotorista c = new CustoMotorista();
            c.setQuilometrosRodados(dto.getQuilometrosRodados());
            c.setValorPorKm(dto.getValorPorKm());
            c.setKmSaida(dto.getKmSaida());
            c.setValorSaida(dto.getValorSaida());
            c.setTotal(dto.getQuilometrosRodados().multiply(dto.getValorPorKm()).add(dto.getValorSaida()));
            c.setFinanceiro(financeiro);
            return c;
        }).collect(Collectors.toList());
    }

    private List<CustoPedagio> mapCustoPedagio(List<CustoPedagioDTO> dtos, Financeiro financeiro) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            CustoPedagio c = new CustoPedagio();
            c.setSinistro(dto.getSinistro());
            c.setQuantidade(dto.getQuantidade());
            c.setValor(dto.getValor());
            c.setTotal(dto.getValor().multiply(new BigDecimal(dto.getQuantidade())));
            c.setFinanceiro(financeiro);
            return c;
        }).collect(Collectors.toList());
    }

    private List<CustoPatins> mapCustoPatins(List<CustoPatinsDTO> dtos, Financeiro financeiro) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            CustoPatins c = new CustoPatins();
            c.setSinistro(dto.getSinistro());
            c.setValor(dto.getValor());
            c.setFinanceiro(financeiro);
            return c;
        }).collect(Collectors.toList());
    }

    private List<CustoHoraParada> mapCustoHoraParada(List<CustoHoraParadaDTO> dtos, Financeiro financeiro) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            CustoHoraParada c = new CustoHoraParada();
            c.setHoras(dto.getHoras());
            c.setValorHoraParada(dto.getValorHoraParada());
            c.setValorTotal(dto.getHoras().multiply(dto.getValorHoraParada()));
            c.setFinanceiro(financeiro);
            return c;
        }).collect(Collectors.toList());
    }

    private List<CustoHoraTrabalhada> mapCustoHoraTrabalhada(List<CustoHoraTrabalhadaDTO> dtos, Financeiro financeiro) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            CustoHoraTrabalhada c = new CustoHoraTrabalhada();
            c.setHoras(dto.getHoras());
            c.setValorHoraTrabalhada(dto.getValorHoraTrabalhada());
            c.setValorTotal(dto.getHoras().multiply(dto.getValorHoraTrabalhada()));
            c.setFinanceiro(financeiro);
            return c;
        }).collect(Collectors.toList());
    }

    private List<CustoDiaria> mapCustoDiaria(List<CustoDiariaDTO> dtos, Financeiro financeiro) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            CustoDiaria c = new CustoDiaria();
            c.setSinistro(dto.getSinistro());
            c.setEntrada(dto.getEntrada());
            c.setSaida(dto.getSaida());
            long estadia = ChronoUnit.DAYS.between(dto.getEntrada(), dto.getSaida());
            c.setEstadia(estadia);
            c.setValorPorDia(dto.getValorPorDia());
            c.setValorTotal(dto.getValorPorDia().multiply(new BigDecimal(estadia)));
            c.setFinanceiro(financeiro);
            return c;
        }).collect(Collectors.toList());
    }

    private List<CustoRodaExtra> mapCustoRodaExtra(List<CustoRodaExtraDTO> dtos, Financeiro financeiro) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            CustoRodaExtra c = new CustoRodaExtra();
            c.setSinistro(dto.getSinistro());
            c.setValor(dto.getValor());
            c.setFinanceiro(financeiro);
            return c;
        }).collect(Collectors.toList());
    }

    private List<CustoExcedente> mapCustoExcedente(List<CustoExcedenteDTO> dtos, Financeiro financeiro) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            CustoExcedente c = new CustoExcedente();
            c.setExcedentes(dto.getExcedentes());
            c.setObservacao(dto.getObservacao());
            c.setFinanceiro(financeiro);
            return c;
        }).collect(Collectors.toList());
    }
}
