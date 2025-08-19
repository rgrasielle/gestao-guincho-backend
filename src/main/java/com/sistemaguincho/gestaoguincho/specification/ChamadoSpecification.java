package com.sistemaguincho.gestaoguincho.specification;

import com.sistemaguincho.gestaoguincho.entity.Chamado;
import com.sistemaguincho.gestaoguincho.enums.Status;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;

public class ChamadoSpecification {

    public static Specification<Chamado> filtrar(
            String sinistro,
            String placa,
            Long id,
            Status status,
            String tipoServico,
            String modeloVeiculo,
            String seguradora,
            OffsetDateTime dataAbertura,
            OffsetDateTime dataFechamento
    ) {
        return (root, query, builder) -> {
            var predicates = builder.conjunction();

            if (sinistro != null)
                predicates = builder.and(predicates, builder.like(root.get("sinistro"), "%" + sinistro + "%"));
            if (placa != null)
                predicates = builder.and(predicates, builder.like(root.join("guincho").get("placa"), "%" + placa + "%"));
            if (id != null)
                predicates = builder.and(predicates, builder.equal(root.get("id"), id));
            if (status != null)
                predicates = builder.and(predicates, builder.equal(root.get("status"), status));
            if (tipoServico != null)
                predicates = builder.and(predicates, builder.equal(root.get("tipoServico"), tipoServico));
            if (modeloVeiculo != null)
                predicates = builder.and(predicates, builder.like(root.get("veiculoModelo"), "%" + modeloVeiculo + "%"));
            if (seguradora != null)
                predicates = builder.and(predicates, builder.like(root.get("seguradora"), "%" + seguradora + "%"));
            if (dataAbertura != null)
                predicates = builder.and(predicates, builder.greaterThanOrEqualTo(root.get("dataAbertura"), dataAbertura));
            if (dataFechamento != null)
                predicates = builder.and(predicates, builder.lessThanOrEqualTo(root.get("dataFechamento"), dataFechamento));

            return predicates;
        };
    }
}
