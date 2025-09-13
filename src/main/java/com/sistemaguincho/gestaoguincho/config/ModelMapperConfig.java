package com.sistemaguincho.gestaoguincho.config;

import com.sistemaguincho.gestaoguincho.dto.ChamadoRequestDTO;
import com.sistemaguincho.gestaoguincho.dto.ChamadoResponseDTO;
import com.sistemaguincho.gestaoguincho.dto.GuinchoRequestDTO;
import com.sistemaguincho.gestaoguincho.dto.MotoristaRequestDTO;
import com.sistemaguincho.gestaoguincho.entity.Chamado;
import com.sistemaguincho.gestaoguincho.entity.Guincho;
import com.sistemaguincho.gestaoguincho.entity.Motorista;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Mapeamento DTO -> Entidade (continua igual)
        mapper.addMappings(new PropertyMap<ChamadoRequestDTO, Chamado>() {
            @Override
            protected void configure() {
                skip(destination.getId());
                skip(destination.getMotorista());
                skip(destination.getGuincho());
            }
        });

        // Cria um conversor explícito para Chamado -> ChamadoResponseDTO
        Converter<Chamado, ChamadoResponseDTO> chamadoToDtoConverter = new Converter<Chamado, ChamadoResponseDTO>() {
            public ChamadoResponseDTO convert(MappingContext<Chamado, ChamadoResponseDTO> context) {
                Chamado source = context.getSource();
                ChamadoResponseDTO destination = new ChamadoResponseDTO();

                // Mapeamento campo a campo
                destination.setId(source.getId());
                destination.setStatus(source.getStatus());
                destination.setDataAcionamento(source.getDataAcionamento());
                destination.setHora(source.getHora());
                destination.setClienteNome(source.getClienteNome());
                destination.setClienteTelefone(source.getClienteTelefone());
                destination.setVeiculoModelo(source.getVeiculoModelo());
                destination.setVeiculoAno(source.getVeiculoAno());
                destination.setVeiculoPlaca(source.getVeiculoPlaca());
                destination.setSinistro(source.getSinistro());
                destination.setSeguradora(source.getSeguradora());
                destination.setTipoServico(source.getTipoServico());

                destination.setCreatedAt(source.getCreatedAt());

                // Mapeamento dos relacionamentos
                if (source.getMotorista() != null) {
                    destination.setMotoristaNome(source.getMotorista().getNome());
                }

                if (source.getGuincho() != null) {
                    destination.setGuinchoDescricao(
                            source.getGuincho().getModelo() + " - " + source.getGuincho().getPlaca()
                    );
                }

                return destination;
            }
        };

        // Adiciona o conversor ao ModelMapper
        mapper.createTypeMap(Chamado.class, ChamadoResponseDTO.class).setConverter(chamadoToDtoConverter);

        // Configuração de segurança para GuinchoRequestDTO -> Guincho
        mapper.typeMap(GuinchoRequestDTO.class, Guincho.class).addMappings(m -> {
            m.skip(Guincho::setId);
        });

        // Configuração de segurança para MotoristaRequestDTO -> Motorista
        mapper.typeMap(MotoristaRequestDTO.class, Motorista.class).addMappings(m -> {
            m.skip(Motorista::setId);
        });

        return mapper;
    }
}