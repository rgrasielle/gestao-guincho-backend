package com.sistemaguincho.gestaoguincho.service;

import com.sistemaguincho.gestaoguincho.dto.MotoristaRequestDTO;
import com.sistemaguincho.gestaoguincho.dto.MotoristaResponseDTO;
import com.sistemaguincho.gestaoguincho.entity.Motorista;
import com.sistemaguincho.gestaoguincho.enums.Disponibilidade;
import com.sistemaguincho.gestaoguincho.repository.MotoristaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MotoristaService {
    private final MotoristaRepository repository;
    private final ModelMapper modelMapper;

    public MotoristaService(MotoristaRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public MotoristaResponseDTO criar(MotoristaRequestDTO dto) {
        Motorista motorista = modelMapper.map(dto, Motorista.class);
        Motorista salvo = repository.save(motorista);
        return modelMapper.map(salvo, MotoristaResponseDTO.class);
    }

    public List<MotoristaResponseDTO> listar() {
        return repository.findAll().stream()
                .map(m -> modelMapper.map(m, MotoristaResponseDTO.class))
                .collect(Collectors.toList());
    }

    public MotoristaResponseDTO atualizar(Long id, MotoristaRequestDTO dto) {
        Motorista motorista = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado"));
        modelMapper.map(dto, motorista);
        Motorista atualizado = repository.save(motorista);
        return modelMapper.map(atualizado, MotoristaResponseDTO.class);
    }

    public MotoristaResponseDTO atualizarDisponibilidade(Long id, Disponibilidade disponibilidade) {
        Motorista motorista = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado"));
        motorista.setDisponibilidade(disponibilidade);
        Motorista atualizado = repository.save(motorista);
        return modelMapper.map(atualizado, MotoristaResponseDTO.class);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Motorista não encontrado");
        }
        repository.deleteById(id);
    }
}
