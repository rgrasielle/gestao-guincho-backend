package com.sistemaguincho.gestaoguincho.specification;

import com.sistemaguincho.gestaoguincho.entity.Chamado;
import com.sistemaguincho.gestaoguincho.entity.Motorista;
import com.sistemaguincho.gestaoguincho.enums.Status;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class ChamadoSpecification {

    public static Specification<Chamado> filtrar(
            String busca,
            Status status,
            String tipoServico,
            String seguradora,
            Long motoristaId,
            LocalDate dataServicoInicio,
            LocalDate dataServicoFim
    ) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (busca != null && !busca.isBlank()) {
                String buscaLower = "%" + busca.toLowerCase() + "%";

                // Lista para os predicados da cláusula OR
                List<Predicate> orPredicates = new ArrayList<>();

                // Adiciona os predicados de busca para campos de TEXTO
                orPredicates.add(builder.like(builder.lower(root.get("sinistro")), buscaLower));
                orPredicates.add(builder.like(builder.lower(root.get("veiculoPlaca")), buscaLower));
                orPredicates.add(builder.like(builder.lower(root.get("clienteNome")), buscaLower));

                // ========== LÓGICA PARA O ID ==========

                // Tenta converter a string de busca para um número (Long).
                // Se conseguir, adiciona uma comparação de IGUALDADE para o ID.
                try {
                    Long idBusca = Long.parseLong(busca.trim());
                    orPredicates.add(builder.equal(root.get("id"), idBusca));
                } catch (NumberFormatException e) {
                    // Se a busca não for um número (ex: "joao"), ignora a busca pelo ID,
                    // o que é o comportamento correto.
                }

                // Junta todos os predicados da busca com OR
                predicates.add(builder.or(orPredicates.toArray(new Predicate[0])));
            }

            // Adiciona os outros filtros com AND
            if (status != null)
                predicates.add(builder.equal(root.get("status"), status));

            if (tipoServico != null && !tipoServico.isBlank())
                predicates.add(builder.equal(root.get("tipoServico"), tipoServico));

            if (seguradora != null && !seguradora.isBlank())
                predicates.add(builder.equal(root.get("seguradora"), seguradora));

            if (motoristaId != null) {
                Join<Chamado, Motorista> motoristaJoin = root.join("motorista");
                predicates.add(builder.equal(motoristaJoin.get("id"), motoristaId));
            }

            // ========== LÓGICA DE FILTRO DE DATA ==========

            // Cenário 1: Apenas a data de início é fornecida (busca por um dia específico)
            if (dataServicoInicio != null && dataServicoFim == null) {
                predicates.add(builder.equal(root.get("dataServico"), dataServicoInicio));
            }

            // Cenário 2: Ambas as datas são fornecidas (busca por um intervalo)
            if (dataServicoInicio != null && dataServicoFim != null) {
                predicates.add(builder.between(root.get("dataServico"), dataServicoInicio, dataServicoFim));
            }

            // Cenário 3 (Bônus): Apenas a data de fim é fornecida (busca tudo até aquela data)
            if (dataServicoInicio == null && dataServicoFim != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("dataServico"), dataServicoFim));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}