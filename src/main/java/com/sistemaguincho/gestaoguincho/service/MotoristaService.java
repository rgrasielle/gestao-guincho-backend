package com.sistemaguincho.gestaoguincho.service;

import com.sistemaguincho.gestaoguincho.dto.MotoristaRequestDTO;
import com.sistemaguincho.gestaoguincho.dto.MotoristaResponseDTO;
import com.sistemaguincho.gestaoguincho.entity.Motorista;
import com.sistemaguincho.gestaoguincho.enums.Disponibilidade;
import com.sistemaguincho.gestaoguincho.enums.Status;
import com.sistemaguincho.gestaoguincho.repository.ChamadoRepository;
import com.sistemaguincho.gestaoguincho.repository.MotoristaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MotoristaService {
    private final MotoristaRepository repository;
    private final ChamadoRepository chamadoRepository; // Novo
    private final ModelMapper modelMapper;

    public MotoristaService(MotoristaRepository repository,
                            ChamadoRepository chamadoRepository,
                            ModelMapper modelMapper) {
        this.repository = repository;
        this.chamadoRepository = chamadoRepository;
        this.modelMapper = modelMapper;
    }

    // Criar
    public MotoristaResponseDTO criar(MotoristaRequestDTO dto) {
        Motorista motorista = modelMapper.map(dto, Motorista.class);

        // Define disponibilidade padrão
        motorista.setDisponibilidade(Disponibilidade.DISPONIVEL);

        Motorista salvo = repository.save(motorista);
        return modelMapper.map(salvo, MotoristaResponseDTO.class);
    }

    // Listar
    public List<MotoristaResponseDTO> listar() {
        return repository.findAll().stream()
                .map(m -> modelMapper.map(m, MotoristaResponseDTO.class))
                .collect(Collectors.toList());
    }

    // Atualizar
    public MotoristaResponseDTO atualizar(Long id, MotoristaRequestDTO dto) {
        Motorista motorista = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado"));
        modelMapper.map(dto, motorista);
        Motorista atualizado = repository.save(motorista);
        return modelMapper.map(atualizado, MotoristaResponseDTO.class);
    }

    // Atualizar disponibilidade
    public MotoristaResponseDTO atualizarDisponibilidade(Long id, Disponibilidade disponibilidade) {
        Motorista motorista = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado"));
        motorista.setDisponibilidade(disponibilidade);
        Motorista atualizado = repository.save(motorista);
        return modelMapper.map(atualizado, MotoristaResponseDTO.class);
    }

    // Atualizar disponibilidade automaticamente
    public void atualizarDisponibilidadeAutomatica(Long motoristaId) {
        Motorista motorista = repository.findById(motoristaId)
                .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado"));

        boolean emAndamento = chamadoRepository.existsByMotoristaIdAndStatus(motoristaId, Status.EM_ANDAMENTO);
        boolean reservado = chamadoRepository.existsByMotoristaIdAndStatus(motoristaId, Status.ABERTO);

        if (emAndamento) {
            motorista.setDisponibilidade(Disponibilidade.EM_ATENDIMENTO);
        } else if (reservado) {
            motorista.setDisponibilidade(Disponibilidade.RESERVADO);
        } else {
            motorista.setDisponibilidade(Disponibilidade.DISPONIVEL);
        }

        repository.save(motorista);
    }

    // Deletar
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Motorista não encontrado");
        }
        repository.deleteById(id);
    }
}
