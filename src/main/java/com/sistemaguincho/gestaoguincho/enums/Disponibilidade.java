package com.sistemaguincho.gestaoguincho.enums;

public enum Disponibilidade {
    DISPONIVEL,        // Não vinculado a nenhum chamado
    RESERVADO,         // Vinculado a um chamado ABERTO (agendado)
    EM_ATENDIMENTO,    // Chamado EM ANDAMENTO
    INDISPONIVEL       // Pode manter se houver casos especiais de indisponibilidade
}
