package com.sistemaguincho.gestaoguincho.config;

import com.sistemaguincho.gestaoguincho.dto.ChamadoRequestDTO;
import com.sistemaguincho.gestaoguincho.dto.ChamadoResponseDTO;
import com.sistemaguincho.gestaoguincho.entity.Chamado;
import com.sistemaguincho.gestaoguincho.entity.Motorista;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Ignorar ID e relacionamentos no mapeamento DTO -> Entidade
        mapper.addMappings(new PropertyMap<ChamadoRequestDTO, Chamado>() {
            @Override
            protected void configure() {
                skip(destination.getId());          // Ignora o ID
                skip(destination.getMotorista());   // Ignora motorista
                skip(destination.getGuincho());     // Ignora guincho
            }
        });

        // Mapeamento para Chamado -> ChamadoResponseDTO
        mapper.addMappings(new PropertyMap<Chamado, ChamadoResponseDTO>() {
            @Override
            protected void configure() {
                using(ctx -> {
                    Motorista motorista = ((Chamado) ctx.getSource()).getMotorista();
                    return motorista != null ? motorista.getNome() : null;
                }).map(source, destination.getMotorista());
                // Você pode adicionar outros campos personalizados aqui se necessário
            }
        });

        return mapper;
    }
}

