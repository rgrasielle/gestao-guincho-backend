package com.sistemaguincho.gestaoguincho.enums;

public enum Status {
    ABERTO,        // Chamado recém-criado
    EM_ANDAMENTO,  // Guincho a caminho / motorista designado
    FINALIZADO,     // Serviço finalizado
    CANCELADO      // Caso seja cancelado antes da conclusão
}
