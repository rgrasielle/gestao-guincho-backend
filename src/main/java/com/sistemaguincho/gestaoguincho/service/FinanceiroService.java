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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinanceiroService {

    private final FinanceiroRepository financeiroRepository;
    private final ChamadoRepository chamadoRepository;
    private final ModelMapper modelMapper;

    public FinanceiroService(FinanceiroRepository financeiroRepository,
                             ChamadoRepository chamadoRepository,
                             ModelMapper modelMapper) {
        this.financeiroRepository = financeiroRepository;
        this.chamadoRepository = chamadoRepository;
        this.modelMapper = modelMapper;
    }

    // Criar/atualizar financeiro vinculado a um chamado
    public FinanceiroResponseDTO salvarFinanceiro(Long chamadoId, FinanceiroRequestDTO dto) {
        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new EntityNotFoundException("Chamado não encontrado"));

        Financeiro financeiro = new Financeiro();
        financeiro.setChamado(chamado);

        // Mapear custos das entidades filhas usando ModelMapper e calcular valores parciais
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

        // Calcula o total final somando todos os custos
        financeiro.calcularTotalFinal();

        financeiro = financeiroRepository.save(financeiro);

        // Mapear entidade Financeiro → DTO de resposta usando ModelMapper
        return modelMapper.map(financeiro, FinanceiroResponseDTO.class);
    }

    // Buscar financeiro de um chamado específico
    public FinanceiroResponseDTO buscarPorChamado(Long chamadoId) {
        Financeiro financeiro = financeiroRepository.findByChamadoId(chamadoId)
                .orElseThrow(() -> new EntityNotFoundException("Financeiro não encontrado para este chamado"));

        return modelMapper.map(financeiro, FinanceiroResponseDTO.class);
    }

    // ---------- Métodos privados para mapear e calcular cada custo ----------

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
