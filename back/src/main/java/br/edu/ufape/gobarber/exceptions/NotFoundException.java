package br.edu.ufape.gobarber.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando um recurso não é encontrado.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String entityName, Long id) {
        super(entityName + " não encontrado(a) com ID: " + id);
    }

    public NotFoundException(String entityName, String field, String value) {
        super(entityName + " não encontrado(a) com " + field + ": " + value);
    }
}
