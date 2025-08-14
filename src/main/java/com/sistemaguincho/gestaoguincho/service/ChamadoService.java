package com.sistemaguincho.gestaoguincho.service;

import com.sistemaguincho.gestaoguincho.config.ModelMapperConfig;
import com.sistemaguincho.gestaoguincho.dto.ChamadoRequestDTO;
import com.sistemaguincho.gestaoguincho.dto.ChamadoResponseDTO;
import com.sistemaguincho.gestaoguincho.entity.Chamado;
import com.sistemaguincho.gestaoguincho.entity.Motorista;
import com.sistemaguincho.gestaoguincho.repository.ChamadoRepository;
import com.sistemaguincho.gestaoguincho.repository.MotoristaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final MotoristaRepository motoristaRepository;
    private final ModelMapper modelMapper;

    public ChamadoService(
            ChamadoRepository chamadoRepository,
            MotoristaRepository motoristaRepository,
            ModelMapper modelMapper
    ) {
        this.chamadoRepository = chamadoRepository;
        this.motoristaRepository = motoristaRepository;
        this.modelMapper = modelMapper;
    }

    // Criar chamado
    public ChamadoResponseDTO criarChamado(ChamadoRequestDTO dto) {
        Chamado chamado = modelMapper.map(dto, Chamado.class);

        // Busca motorista
        Motorista motorista = motoristaRepository.findById(dto.getMotoristaId())
                .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado"));
        chamado.setMotorista(motorista);

        Chamado salvo = chamadoRepository.save(chamado);
        return modelMapper.map(salvo, ChamadoResponseDTO.class);
    }

    // Listar chamados (com filtros opcionais)
    public Page<ChamadoResponseDTO> listarChamados(
            String sinistro,
            String placa,
            String codigo,
            String status,
            String tipo,
            String veiculo,
            String cliente,
            String seguradora,
            LocalDate entrada,
            LocalDate saida,
            Pageable pageable
    ) {
        Page<Chamado> chamados = chamadoRepository.buscarPorFiltros(
                sinistro, placa, codigo, status, tipo, veiculo, cliente, seguradora, entrada, saida, pageable
        );
        return chamados.map(ch -> modelMapper.map(ch, ChamadoResponseDTO.class));
    }

    // Buscar chamado por ID
    public ChamadoResponseDTO buscarPorId(Long id) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chamado não encontrado"));
        return modelMapper.map(chamado, ChamadoResponseDTO.class);
    }

    // Atualizar chamado
    public ChamadoResponseDTO atualizarChamado(Long id, ChamadoRequestDTO dto) {
        Chamado chamadoExistente = chamadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chamado não encontrado"));

        modelMapper.map(dto, chamadoExistente); // Atualiza campos

        Motorista motorista = motoristaRepository.findById(dto.getMotoristaId())
                .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado"));
        chamadoExistente.setMotorista(motorista);

        Chamado atualizado = chamadoRepository.save(chamadoExistente);
        return modelMapper.map(atualizado, ChamadoResponseDTO.class);
    }

    // Atualizar status
    public ChamadoResponseDTO atualizarStatus(Long id, String status) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chamado não encontrado"));

        chamado.setStatus(status);
        Chamado atualizado = chamadoRepository.save(chamado);
        return modelMapper.map(atualizado, ChamadoResponseDTO.class);
    }

    // Deletar chamado
    public void deletarChamado(Long id) {
        if (!chamadoRepository.existsById(id)) {
            throw new EntityNotFoundException("Chamado não encontrado");
        }
        chamadoRepository.deleteById(id);
    }
}
