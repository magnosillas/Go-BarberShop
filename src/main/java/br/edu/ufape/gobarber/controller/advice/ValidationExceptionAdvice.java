package br.edu.ufape.gobarber.controller.advice;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller Advice para tratamento centralizado de exceções de validação.
 * Segue o padrão da arquitetura base do projeto Transporte.
 * 
 * Intercepta exceções de validação do Bean Validation (@Valid)
 * e retorna uma resposta padronizada com detalhes dos campos inválidos.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ValidationExceptionAdvice extends ResponseEntityExceptionHandler {

    /**
     * Trata exceções de validação de argumentos de método.
     * Captura erros de @Valid e @Validated nos controllers.
     * 
     * @param ex Exceção de validação
     * @param headers Headers HTTP
     * @param status Status HTTP
     * @param request Requisição web
     * @return ResponseEntity com ErrorResponse detalhado
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, 
            HttpHeaders headers, 
            HttpStatus status, 
            WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
        
        ErrorResponse resposta = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Erro de validação")
                .message("Um ou mais campos estão inválidos. Verifique os detalhes.")
                .path(servletRequest.getRequestURI())
                .details(errors)
                .build();

        return new ResponseEntity<>(resposta, HttpStatus.BAD_REQUEST);
    }
}
