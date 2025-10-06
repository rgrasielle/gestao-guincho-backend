package com.sistemaguincho.gestaoguincho.service;

import com.sistemaguincho.gestaoguincho.dto.GuinchoRequestDTO;
import com.sistemaguincho.gestaoguincho.dto.GuinchoResponseDTO;
import com.sistemaguincho.gestaoguincho.entity.Guincho;
import com.sistemaguincho.gestaoguincho.enums.Disponibilidade;
import com.sistemaguincho.gestaoguincho.enums.Status;
import com.sistemaguincho.gestaoguincho.repository.ChamadoRepository;
import com.sistemaguincho.gestaoguincho.repository.GuinchoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuinchoService {

    private final GuinchoRepository repository;
    private final ChamadoRepository chamadoRepository;
    private final ModelMapper modelMapper;

    public GuinchoService(GuinchoRepository repository, ChamadoRepository chamadoRepository, ModelMapper modelMapper) {
        this.repository = repository;
        this.chamadoRepository = chamadoRepository;
        this.modelMapper = modelMapper;
    }

    // Criar
    public GuinchoResponseDTO criar(GuinchoRequestDTO dto) {
        Guincho guincho = modelMapper.map(dto, Guincho.class);

        // Define disponibilidade padrão
        guincho.setDisponibilidade(Disponibilidade.DISPONIVEL);

        return modelMapper.map(repository.save(guincho), GuinchoResponseDTO.class);
    }

    // Listar
    public List<GuinchoResponseDTO> listar() {
        return repository.findAll().stream()
                .map(g -> modelMapper.map(g, GuinchoResponseDTO.class))
                .collect(Collectors.toList());
    }

    // Atualizar
    public GuinchoResponseDTO atualizar(Long id, GuinchoRequestDTO dto) {
        Guincho existente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Guincho não encontrado"));

        // Atualiza apenas os campos que podem ser alterados pelo usuário
        existente.setModelo(dto.getModelo());
        existente.setPlaca(dto.getPlaca());
        existente.setTipo(dto.getTipo());
        existente.setCapacidade(dto.getCapacidade());

        // NÃO altera a disponibilidade, mantém o valor atual do banco

        Guincho atualizado = repository.save(existente);
        return modelMapper.map(atualizado, GuinchoResponseDTO.class);
    }

    // Atualizar disponibilidade
    public GuinchoResponseDTO atualizarDisponibilidade(Long id, Disponibilidade disponibilidade) {
        Guincho guincho = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Guincho não encontrado"));
        guincho.setDisponibilidade(disponibilidade);
        Guincho atualizado = repository.save(guincho);
        return modelMapper.map(atualizado, GuinchoResponseDTO.class);
    }

    // Atualizar disponibilidade automaticamente
    public void atualizarDisponibilidadeAutomatica(Long guinchoId) {
        Guincho guincho = repository.findById(guinchoId)
                .orElseThrow(() -> new EntityNotFoundException("Guincho não encontrado"));

        boolean emAndamento = chamadoRepository.existsByGuinchoIdAndStatus(guinchoId, Status.EM_ANDAMENTO);
        boolean reservado = chamadoRepository.existsByGuinchoIdAndStatus(guinchoId, Status.ABERTO);

        if (emAndamento) {
            guincho.setDisponibilidade(Disponibilidade.EM_ATENDIMENTO);
        } else if (reservado) {
            guincho.setDisponibilidade(Disponibilidade.RESERVADO);
        } else {
            guincho.setDisponibilidade(Disponibilidade.DISPONIVEL);
        }

        repository.save(guincho);
    }

    // Deletar
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Guincho não encontrado");
        }
        repository.deleteById(id);
    }
}
