package com.sistemaguincho.gestaoguincho.enums;

public enum Status {
    ABERTO,        // Chamado recém-criado
    EM_ANDAMENTO,  // Guincho a caminho / motorista designado
    CONCLUIDO,     // Serviço finalizado
    CANCELADO      // Caso seja cancelado antes da conclusão
}
