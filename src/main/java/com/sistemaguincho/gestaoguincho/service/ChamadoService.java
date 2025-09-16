package com.sistemaguincho.gestaoguincho.service;

import com.sistemaguincho.gestaoguincho.dto.ChamadoRequestDTO;
import com.sistemaguincho.gestaoguincho.dto.ChamadoResponseDTO;
import com.sistemaguincho.gestaoguincho.entity.Chamado;
import com.sistemaguincho.gestaoguincho.entity.Guincho;
import com.sistemaguincho.gestaoguincho.entity.Motorista;
import com.sistemaguincho.gestaoguincho.enums.Disponibilidade;
import com.sistemaguincho.gestaoguincho.enums.Status;
import com.sistemaguincho.gestaoguincho.repository.ChamadoRepository;
import com.sistemaguincho.gestaoguincho.repository.FinanceiroRepository;
import com.sistemaguincho.gestaoguincho.repository.GuinchoRepository;
import com.sistemaguincho.gestaoguincho.repository.MotoristaRepository;
import com.sistemaguincho.gestaoguincho.specification.ChamadoSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

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

        // Lógica de atualização de Disponibilidade
        Motorista motoristaAtribuido = motoristaRepository.findById(dto.getServico().getMotoristaId())
                .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado"));
        Guincho guinchoAtribuido = guinchoRepository.findById(dto.getServico().getGuinchoId())
                .orElseThrow(() -> new EntityNotFoundException("Guincho não encontrado"));

        motoristaAtribuido.setDisponibilidade(Disponibilidade.EM_ATENDIMENTO);
        guinchoAtribuido.setDisponibilidade(Disponibilidade.EM_ATENDIMENTO);

        motoristaRepository.save(motoristaAtribuido);
        guinchoRepository.save(guinchoAtribuido);


        Chamado chamado = mapDtoParaEntidade(dto, new Chamado(), motoristaAtribuido, guinchoAtribuido);
        chamado.setStatus(Status.ABERTO);
        chamado.setDataAbertura(OffsetDateTime.now());

        Chamado salvo = chamadoRepository.save(chamado);
        return modelMapper.map(salvo, ChamadoResponseDTO.class);
    }

    // -------------------- ATUALIZAR CHAMADO --------------------
    public ChamadoResponseDTO atualizarChamado(Long id, ChamadoRequestDTO dto) {
        Chamado chamadoExistente = chamadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chamado não encontrado"));

        // Lógica de atualização de Disponibilidade
        // Libera o motorista e guincho antigos se eles forem trocados
        liberarRecursosSeNecessario(chamadoExistente, dto);

        // Pega o motorista e o guincho que serão atribuídos (ou reatribuídos)
        Motorista motoristaNovo = motoristaRepository.findById(dto.getServico().getMotoristaId())
                .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado"));
        Guincho guinchoNovo = guinchoRepository.findById(dto.getServico().getGuinchoId())
                .orElseThrow(() -> new EntityNotFoundException("Guincho não encontrado"));

        motoristaNovo.setDisponibilidade(Disponibilidade.EM_ATENDIMENTO);
        guinchoNovo.setDisponibilidade(Disponibilidade.EM_ATENDIMENTO);

        motoristaRepository.save(motoristaNovo);
        guinchoRepository.save(guinchoNovo);

        // Atualiza os campos manualmente
        mapDtoParaEntidade(dto, chamadoExistente, motoristaNovo, guinchoNovo);

        Chamado atualizado = chamadoRepository.save(chamadoExistente);
        return modelMapper.map(atualizado, ChamadoResponseDTO.class);
    }

    // -------------------- ATUALIZAR STATUS --------------------
    @Transactional
    public ChamadoResponseDTO atualizarStatus(Long id, Status novoStatus) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chamado não encontrado"));

        chamado.setStatus(novoStatus);

        Motorista motorista = chamado.getMotorista();
        Guincho guincho = chamado.getGuincho();

        if (novoStatus == Status.FINALIZADO) {
            chamado.setDataFechamento(OffsetDateTime.now());

            // Se o motorista existir, define como DISPONIVEL
            if (motorista != null) {
                motorista.setDisponibilidade(Disponibilidade.DISPONIVEL);
                motoristaRepository.save(motorista);
            }
            // Se o guincho existir, define como DISPONIVEL
            if (guincho != null) {
                guincho.setDisponibilidade(Disponibilidade.DISPONIVEL);
                guinchoRepository.save(guincho);
            }

        } else {
            chamado.setDataFechamento(null);

            // Se o chamado for reaberto, motorista e guincho voltam a ficar ocupados
            if (motorista != null) {
                motorista.setDisponibilidade(Disponibilidade.EM_ATENDIMENTO);
                motoristaRepository.save(motorista);
            }
            if (guincho != null) {
                guincho.setDisponibilidade(Disponibilidade.EM_ATENDIMENTO);
                guinchoRepository.save(guincho);
            }
        }

        Chamado atualizado = chamadoRepository.save(chamado);
        return modelMapper.map(atualizado, ChamadoResponseDTO.class);
    }

    // -------------------- LISTAR CHAMADOS --------------------
    public Page<ChamadoResponseDTO> listarChamados(
            String busca,
            Status status,
            String tipoServico,
            String seguradora,
            Long motoristaId,
            LocalDate dataServicoInicio,
            LocalDate dataServicoFim,
            Pageable pageable
    ) {
        Page<Chamado> chamadosPage = chamadoRepository.findAll(
                ChamadoSpecification.filtrar(
                        busca, status, tipoServico, seguradora, motoristaId, dataServicoInicio, dataServicoFim
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

    private Chamado mapDtoParaEntidade(ChamadoRequestDTO dto, Chamado chamado, Motorista motorista, Guincho guincho) {
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
        chamado.setDataServico(dto.getServico().getDataServico());
        chamado.setHora(dto.getServico().getHora());
        chamado.setTipoServico(dto.getServico().getTipoServico());

        // Motorista
        chamado.setMotorista(motorista);

        // Guincho
        chamado.setGuincho(guincho);

        // Observações
        chamado.setObservacoes(dto.getObservacoes());

        return chamado;
    }

    // Método para lidar com a troca de recursos na atualização
    private void liberarRecursosSeNecessario(Chamado chamadoExistente, ChamadoRequestDTO dto) {
        // Libera motorista antigo se for diferente do novo
        if (chamadoExistente.getMotorista() != null &&
                !Objects.equals(chamadoExistente.getMotorista().getId(), dto.getServico().getMotoristaId())) {

            Motorista motoristaAntigo = chamadoExistente.getMotorista();
            motoristaAntigo.setDisponibilidade(Disponibilidade.DISPONIVEL);
            motoristaRepository.save(motoristaAntigo);
        }

        // Libera guincho antigo se for diferente do novo
        if (chamadoExistente.getGuincho() != null &&
                !Objects.equals(chamadoExistente.getGuincho().getId(), dto.getServico().getGuinchoId())) {

            Guincho guinchoAntigo = chamadoExistente.getGuincho();
            guinchoAntigo.setDisponibilidade(Disponibilidade.DISPONIVEL);
            guinchoRepository.save(guinchoAntigo);
        }
    }

    // -------------------- CONVERT DTO --------------------
    private ChamadoResponseDTO convertToDtoComFinanceiro(Chamado chamado) {
        if (chamado == null) return null;

        ChamadoResponseDTO dto = modelMapper.map(chamado, ChamadoResponseDTO.class);

        // Financeiro
        financeiroRepository.findByChamadoId(chamado.getId())
                .ifPresent(financeiro -> dto.setValorFinal(financeiro.getTotal()));

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
        dto.setDataServico(chamado.getDataServico());
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
