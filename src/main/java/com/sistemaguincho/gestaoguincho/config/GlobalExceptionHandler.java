package com.sistemaguincho.gestaoguincho.config; // Coloque no seu pacote de config ou um pacote de exceptions

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        // Este método será chamado se qualquer parte do código lançar uma AccessDeniedException
        logger.error("============= ERRO DE ACESSO CAPTURADO! =============");
        logger.error("A exceção AccessDeniedException foi lançada.", ex);
        logger.error("=====================================================");

        // Retorna a resposta 403 Forbidden que você está vendo
        return new ResponseEntity<>("Acesso Negado", HttpStatus.FORBIDDEN);
    }
}