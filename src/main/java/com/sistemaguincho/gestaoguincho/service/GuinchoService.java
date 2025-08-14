package com.sistemaguincho.gestaoguincho.service;

import com.sistemaguincho.gestaoguincho.dto.GuinchoRequestDTO;
import com.sistemaguincho.gestaoguincho.dto.GuinchoResponseDTO;
import com.sistemaguincho.gestaoguincho.entity.Guincho;
import com.sistemaguincho.gestaoguincho.repository.GuinchoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuinchoService {

    private final GuinchoRepository repository;
    private final ModelMapper modelMapper;

    public GuinchoService(GuinchoRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public GuinchoResponseDTO criar(GuinchoRequestDTO dto) {
        Guincho guincho = modelMapper.map(dto, Guincho.class);
        return modelMapper.map(repository.save(guincho), GuinchoResponseDTO.class);
    }

    public List<GuinchoResponseDTO> listar() {
        return repository.findAll().stream()
                .map(g -> modelMapper.map(g, GuinchoResponseDTO.class))
                .collect(Collectors.toList());
    }

    public GuinchoResponseDTO atualizar(Long id, GuinchoRequestDTO dto) {
        Guincho existente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Guincho não encontrado"));
        modelMapper.map(dto, existente);
        return modelMapper.map(repository.save(existente), GuinchoResponseDTO.class);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Guincho não encontrado");
        }
        repository.deleteById(id);
    }
}
