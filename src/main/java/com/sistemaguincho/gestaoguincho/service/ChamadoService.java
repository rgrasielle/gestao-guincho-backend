package com.sistemaguincho.gestaoguincho.service;

import com.sistemaguincho.gestaoguincho.dto.ChamadoRequestDTO;
import com.sistemaguincho.gestaoguincho.dto.ChamadoResponseDTO;
import com.sistemaguincho.gestaoguincho.entity.Chamado;
import com.sistemaguincho.gestaoguincho.entity.Guincho;
import com.sistemaguincho.gestaoguincho.entity.Motorista;
import com.sistemaguincho.gestaoguincho.enums.Status;
import com.sistemaguincho.gestaoguincho.repository.ChamadoRepository;
import com.sistemaguincho.gestaoguincho.repository.GuinchoRepository;
import com.sistemaguincho.gestaoguincho.repository.MotoristaRepository;
import com.sistemaguincho.gestaoguincho.specification.ChamadoSpecification;
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
    private final GuinchoRepository guinchoRepository;
    private final ModelMapper modelMapper;

    public ChamadoService(
            ChamadoRepository chamadoRepository,
            MotoristaRepository motoristaRepository,
            GuinchoRepository guinchoRepository,
            ModelMapper modelMapper
    ) {
        this.chamadoRepository = chamadoRepository;
        this.motoristaRepository = motoristaRepository;
        this.guinchoRepository = guinchoRepository;
        this.modelMapper = modelMapper;
    }

    public ChamadoResponseDTO criarChamado(ChamadoRequestDTO dto) {
        // Mapeia DTO → Entidade (ID e relacionamentos já ignorados)
        Chamado chamado = modelMapper.map(dto, Chamado.class);

        // Seta motorista
        Motorista motorista = motoristaRepository.findById(dto.getMotoristaId())
                .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado"));
        chamado.setMotorista(motorista);

        // Seta guincho
        Guincho guincho = guinchoRepository.findById(dto.getGuinchoId())
                .orElseThrow(() -> new EntityNotFoundException("Guincho não encontrado"));
        chamado.setGuincho(guincho);

        // Define status inicial (padrão)
        chamado.setStatus(Status.ABERTO);

        // Salva no banco
        Chamado salvo = chamadoRepository.save(chamado);

        // Retorna DTO de resposta
        return modelMapper.map(salvo, ChamadoResponseDTO.class);
    }

    // Listar chamados (com filtros opcionais)
    public Page<ChamadoResponseDTO> listarChamados(
            String sinistro,
            String placa,
            Long id,
            Status status,
            String tipoServico,
            String modeloVeiculo,
            String seguradora,
            OffsetDateTime dataAbertura,
            OffsetDateTime dataFechamento,
            Pageable pageable
    ) {
        Page<Chamado> chamados = chamadoRepository.findAll(
                ChamadoSpecification.filtrar(
                        sinistro, placa, id, status, tipoServico, modeloVeiculo, seguradora, dataAbertura, dataFechamento
                ),
                pageable
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

        // Atualiza os campos básicos
        modelMapper.map(dto, chamadoExistente);

        // Atualiza motorista
        Motorista motorista = motoristaRepository.findById(dto.getMotoristaId())
                .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado"));
        chamadoExistente.setMotorista(motorista);

        // Atualiza guincho
        Guincho guincho = guinchoRepository.findById(dto.getGuinchoId())
                .orElseThrow(() -> new EntityNotFoundException("Guincho não encontrado"));
        chamadoExistente.setGuincho(guincho);

        Chamado atualizado = chamadoRepository.save(chamadoExistente);
        return modelMapper.map(atualizado, ChamadoResponseDTO.class);
    }

    // Atualizar status
    public ChamadoResponseDTO atualizarStatus(Long id, Status novoStatus) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chamado não encontrado"));

        chamado.setStatus(novoStatus); // Agora só aceita valores válidos do enum

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
