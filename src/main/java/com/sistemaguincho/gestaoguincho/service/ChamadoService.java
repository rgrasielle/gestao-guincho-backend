package com.sistemaguincho.gestaoguincho.service;

import com.sistemaguincho.gestaoguincho.dto.ChamadoRequestDTO;
import com.sistemaguincho.gestaoguincho.dto.ChamadoResponseDTO;
import com.sistemaguincho.gestaoguincho.entity.Chamado;
import com.sistemaguincho.gestaoguincho.entity.Guincho;
import com.sistemaguincho.gestaoguincho.entity.Motorista;
import com.sistemaguincho.gestaoguincho.enums.Status;
import com.sistemaguincho.gestaoguincho.repository.ChamadoRepository;
import com.sistemaguincho.gestaoguincho.repository.FinanceiroRepository;
import com.sistemaguincho.gestaoguincho.repository.GuinchoRepository;
import com.sistemaguincho.gestaoguincho.repository.MotoristaRepository;
import com.sistemaguincho.gestaoguincho.specification.ChamadoSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final MotoristaRepository motoristaRepository;
    private final GuinchoRepository guinchoRepository;
    private final FinanceiroRepository financeiroRepository;
    private final ModelMapper modelMapper;

    public ChamadoService(
            ChamadoRepository chamadoRepository,
            MotoristaRepository motoristaRepository,
            GuinchoRepository guinchoRepository,
            FinanceiroRepository financeiroRepository,
            ModelMapper modelMapper
    ) {
        this.chamadoRepository = chamadoRepository;
        this.motoristaRepository = motoristaRepository;
        this.guinchoRepository = guinchoRepository;
        this.financeiroRepository = financeiroRepository;
        this.modelMapper = modelMapper;
    }

    // -------------------- CRIAR CHAMADO --------------------
    public ChamadoResponseDTO criarChamado(ChamadoRequestDTO dto) {
        Chamado chamado = mapDtoParaEntidade(dto);
        chamado.setStatus(Status.ABERTO);
        chamado.setDataAbertura(OffsetDateTime.now());

        Chamado salvo = chamadoRepository.save(chamado);
        return modelMapper.map(salvo, ChamadoResponseDTO.class);
    }

    // -------------------- ATUALIZAR CHAMADO --------------------
    public ChamadoResponseDTO atualizarChamado(Long id, ChamadoRequestDTO dto) {
        Chamado chamadoExistente = chamadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chamado não encontrado"));

        // Atualiza os campos manualmente
        mapDtoParaEntidade(dto, chamadoExistente);

        Chamado atualizado = chamadoRepository.save(chamadoExistente);
        return modelMapper.map(atualizado, ChamadoResponseDTO.class);
    }

    // -------------------- ATUALIZAR STATUS --------------------
    public ChamadoResponseDTO atualizarStatus(Long id, Status novoStatus) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chamado não encontrado"));

        chamado.setStatus(novoStatus);
        if (novoStatus == Status.FINALIZADO) {
            chamado.setDataFechamento(OffsetDateTime.now());
        }

        Chamado atualizado = chamadoRepository.save(chamado);
        return modelMapper.map(atualizado, ChamadoResponseDTO.class);
    }

    // -------------------- LISTAR CHAMADOS --------------------
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
        Page<Chamado> chamadosPage = chamadoRepository.findAll(
                ChamadoSpecification.filtrar(
                        sinistro, placa, id, status, tipoServico, modeloVeiculo, seguradora, dataAbertura, dataFechamento
                ),
                pageable
        );
        return chamadosPage.map(this::convertToDtoComFinanceiro);
    }

    // -------------------- BUSCAR POR ID --------------------
    public ChamadoResponseDTO buscarPorId(Long id) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chamado não encontrado"));

        return convertToDtoComFinanceiro(chamado);
    }

    // -------------------- DELETAR CHAMADO --------------------
    public void deletarChamado(Long id) {
        if (!chamadoRepository.existsById(id)) {
            throw new EntityNotFoundException("Chamado não encontrado");
        }
        chamadoRepository.deleteById(id);
    }

    // -------------------- LISTAR SEGURADORAS E TIPOS --------------------
    // Retorna uma lista de todas as seguradoras únicas cadastradas
    public List<String> listarSeguradorasDistintas() {
        return chamadoRepository.findDistinctSeguradoras();
    }

    // Retorna uma lista de todos os tipos de serviço únicos cadastrados
    public List<String> listarTiposServicoDistintos() {
        return chamadoRepository.findDistinctTiposServico();
    }

    // -------------------- MÉTODOS AUXILIARES --------------------

    private Chamado mapDtoParaEntidade(ChamadoRequestDTO dto) {
        return mapDtoParaEntidade(dto, new Chamado());
    }

    private Chamado mapDtoParaEntidade(ChamadoRequestDTO dto, Chamado chamado) {
        // Cliente
        chamado.setClienteNome(dto.getCliente().getNome());
        chamado.setClienteCpfCnpj(dto.getCliente().getCpfCnpj());
        chamado.setClienteTelefone(dto.getCliente().getTelefone());
        chamado.setClienteEmail(dto.getCliente().getEmail());
        chamado.setClienteSolicitante(dto.getCliente().getSolicitante());

        // Veículo
        chamado.setVeiculoModelo(dto.getVeiculo().getModelo());
        chamado.setVeiculoAno(dto.getVeiculo().getAno());
        chamado.setVeiculoCor(dto.getVeiculo().getCor());
        chamado.setVeiculoPlaca(dto.getVeiculo().getPlaca());
        chamado.setVeiculoObservacoes(dto.getVeiculo().getObservacoes());

        // Origem
        chamado.setOrigemCep(dto.getOrigem().getCep());
        chamado.setOrigemCidade(dto.getOrigem().getCidade());
        chamado.setOrigemEstado(dto.getOrigem().getEstado());
        chamado.setOrigemBairro(dto.getOrigem().getBairro());
        chamado.setOrigemLogradouro(dto.getOrigem().getLogradouro());
        chamado.setOrigemNumero(dto.getOrigem().getNumero());

        // Destino
        chamado.setDestinoCep(dto.getDestino().getCep());
        chamado.setDestinoCidade(dto.getDestino().getCidade());
        chamado.setDestinoEstado(dto.getDestino().getEstado());
        chamado.setDestinoBairro(dto.getDestino().getBairro());
        chamado.setDestinoLogradouro(dto.getDestino().getLogradouro());
        chamado.setDestinoNumero(dto.getDestino().getNumero());

        // Serviço
        chamado.setSeguradora(dto.getServico().getSeguradora());
        chamado.setSinistro(dto.getServico().getSinistro());
        chamado.setDataAcionamento(dto.getServico().getDataAcionamento());
        chamado.setHora(dto.getServico().getHoraAcionamento());
        chamado.setTipoServico(dto.getServico().getTipoServico());

        // Motorista
        Motorista motorista = motoristaRepository.findById(dto.getServico().getMotoristaId())
                .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado"));
        chamado.setMotorista(motorista);

        // Guincho
        Guincho guincho = guinchoRepository.findById(dto.getServico().getGuinchoId())
                .orElseThrow(() -> new EntityNotFoundException("Guincho não encontrado"));
        chamado.setGuincho(guincho);

        // Observações
        chamado.setObservacoes(dto.getObservacoes());

        return chamado;
    }

    // -------------------- CONVERT DTO --------------------
    private ChamadoResponseDTO convertToDtoComFinanceiro(Chamado chamado) {
        if (chamado == null) return null;

        ChamadoResponseDTO dto = modelMapper.map(chamado, ChamadoResponseDTO.class);

        // Financeiro
        financeiroRepository.findByChamadoId(chamado.getId())
                .ifPresent(financeiro -> dto.setValorFinal(financeiro.getTotalFinal()));

        // Guincho e Motorista
        if (chamado.getGuincho() != null) {
            dto.setGuinchoId(chamado.getGuincho().getId());
            dto.setGuinchoDescricao(chamado.getGuincho().getModelo() + " - " + chamado.getGuincho().getPlaca());
        }
        if (chamado.getMotorista() != null) {
            dto.setMotoristaId(chamado.getMotorista().getId());
            dto.setMotoristaNome(chamado.getMotorista().getNome());
        }

        // Cliente
        dto.setClienteNome(chamado.getClienteNome());
        dto.setClienteTelefone(chamado.getClienteTelefone());
        dto.setClienteEmail(chamado.getClienteEmail());
        dto.setClienteCpfCnpj(chamado.getClienteCpfCnpj());
        dto.setClienteSolicitante(chamado.getClienteSolicitante());

        // Veículo
        dto.setVeiculoModelo(chamado.getVeiculoModelo());
        dto.setVeiculoAno(chamado.getVeiculoAno());
        dto.setVeiculoCor(chamado.getVeiculoCor());
        dto.setVeiculoPlaca(chamado.getVeiculoPlaca());
        dto.setVeiculoObservacoes(chamado.getVeiculoObservacoes());

        // Origem
        dto.setOrigemCep(chamado.getOrigemCep());
        dto.setOrigemCidade(chamado.getOrigemCidade());
        dto.setOrigemEstado(chamado.getOrigemEstado());
        dto.setOrigemBairro(chamado.getOrigemBairro());
        dto.setOrigemLogradouro(chamado.getOrigemLogradouro());
        dto.setOrigemNumero(chamado.getOrigemNumero());

        // Destino
        dto.setDestinoCep(chamado.getDestinoCep());
        dto.setDestinoCidade(chamado.getDestinoCidade());
        dto.setDestinoEstado(chamado.getDestinoEstado());
        dto.setDestinoBairro(chamado.getDestinoBairro());
        dto.setDestinoLogradouro(chamado.getDestinoLogradouro());
        dto.setDestinoNumero(chamado.getDestinoNumero());

        // Serviço
        dto.setSeguradora(chamado.getSeguradora());
        dto.setSinistro(chamado.getSinistro());
        dto.setDataAcionamento(chamado.getDataAcionamento());
        dto.setHora(chamado.getHora());
        dto.setTipoServico(chamado.getTipoServico());

        // Observações
        dto.setObservacoes(chamado.getObservacoes());

        // Campos formatados
        if (chamado.getOrigemLogradouro() != null) {
            dto.setOrigemFormatada(String.format("%s, %s - %s/%s",
                    chamado.getOrigemLogradouro(),
                    chamado.getOrigemNumero(),
                    chamado.getOrigemCidade(),
                    chamado.getOrigemEstado()));
        }

        if (chamado.getDestinoLogradouro() != null) {
            dto.setDestinoFormatado(String.format("%s, %s - %s/%s",
                    chamado.getDestinoLogradouro(),
                    chamado.getDestinoNumero(),
                    chamado.getDestinoCidade(),
                    chamado.getDestinoEstado()));
        }

        return dto;
    }
}
