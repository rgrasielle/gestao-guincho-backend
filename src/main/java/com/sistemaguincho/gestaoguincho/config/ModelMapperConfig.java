package com.sistemaguincho.gestaoguincho.config;

import com.sistemaguincho.gestaoguincho.dto.ChamadoResponseDTO;
import com.sistemaguincho.gestaoguincho.entity.Chamado;
import com.sistemaguincho.gestaoguincho.entity.Motorista;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.hibernate.Hibernate.map;

@Bean
public ModelMapper modelMapper() {
    ModelMapper mapper = new ModelMapper();

    // Mapeamento personalizado com converter seguro
    mapper.addMappings(new PropertyMap<Chamado, ChamadoResponseDTO>() {
        @Override
        protected void configure() {
            using(ctx -> {
                Motorista motorista = ((Chamado) ctx.getSource()).getMotorista();
                return motorista != null ? motorista.getNome() : null;
            }).map(source, destination.getMotorista());
        }
    });

    return mapper;
}

}
