package com.sistemaguincho.gestaoguincho.config;

import com.sistemaguincho.gestaoguincho.dto.ChamadoResponseDTO;
import com.sistemaguincho.gestaoguincho.entity.Chamado;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.hibernate.Hibernate.map;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Mapeamento personalizado: motorista.nome → motorista (String)
        mapper.addMappings(new PropertyMap<Chamado, ChamadoResponseDTO>() {
            @Override
            protected void configure() {
                map().setMotorista(source.getMotorista() != null ? source.getMotorista().getNome() : null);
            }
        });

        return mapper;
    }

}
