package br.edu.ufape.gobarber.controller.advice;

import br.edu.ufape.gobarber.exceptions.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.Instant;

/**
 * Controller Advice global para tratamento centralizado de exceções de negócio.
 * Segue o padrão da arquitetura base do projeto Transporte.
 * 
 * Centraliza o tratamento de todas as exceções da aplicação,
 * garantindo respostas padronizadas e consistentes.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Order
@ControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * Trata exceções de banco de dados genéricas.
     * 
     * @param exception Exceção de banco de dados
     * @param request Requisição HTTP
     * @return ResponseEntity com ErrorResponse
     */
    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<ErrorResponse> handleDataBaseException(
            DataBaseException exception,
            HttpServletRequest request) {
        
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Erro de banco de dados")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Trata exceções de constraint do banco de dados.
     * 
     * @param exception Exceção de constraint
     * @param request Requisição HTTP
     * @return ResponseEntity com ErrorResponse
     */
    @ExceptionHandler(DataBaseConstraintException.class)
    public ResponseEntity<ErrorResponse> handleDataBaseConstraintException(
            DataBaseConstraintException exception,
            HttpServletRequest request) {
        
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Violação de constraint")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Trata exceções de serviço já existente (registro duplicado).
     * 
     * @param exception Exceção de serviço duplicado
     * @param request Requisição HTTP
     * @return ResponseEntity com ErrorResponse
     */
    @ExceptionHandler(ServiceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleServiceAlreadyExistsException(
            ServiceAlreadyExistsException exception,
            HttpServletRequest request) {
        
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Registro duplicado")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Trata exceções de recurso não encontrado.
     * 
     * @param exception Exceção de recurso não encontrado
     * @param request Requisição HTTP
     * @return ResponseEntity com ErrorResponse
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            HttpServletRequest request) {
        
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Recurso não encontrado")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Trata exceções de constraint de validação e argumentos inválidos.
     * 
     * @param exception Exceção de validação
     * @param request Requisição HTTP
     * @return ResponseEntity com ErrorResponse
     */
    @ExceptionHandler({ConstraintViolationException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(
            Exception exception,
            HttpServletRequest request) {
        
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Erro de validação")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Trata exceções de agendamento.
     * 
     * @param exception Exceção de agendamento
     * @param request Requisição HTTP
     * @return ResponseEntity com ErrorResponse
     */
    @ExceptionHandler(AppointmentException.class)
    public ResponseEntity<ErrorResponse> handleAppointmentException(
            AppointmentException exception,
            HttpServletRequest request) {
        
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_ACCEPTABLE.value())
                .error("Erro de agendamento")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Trata exceções de role inválida.
     * 
     * @param exception Exceção de role inválida
     * @param request Requisição HTTP
     * @return ResponseEntity com ErrorResponse
     */
    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRoleException(
            InvalidRoleException exception,
            HttpServletRequest request) {
        
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Role inválida")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Trata exceções de processamento de perfil.
     * 
     * @param exception Exceção de processamento de perfil
     * @param request Requisição HTTP
     * @return ResponseEntity com ErrorResponse
     */
    @ExceptionHandler(ProfileProcessingException.class)
    public ResponseEntity<ErrorResponse> handleProfileProcessingException(
            ProfileProcessingException exception,
            HttpServletRequest request) {
        
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Erro de processamento de perfil")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Trata exceções de parsing JSON.
     * 
     * @param exception Exceção de parsing JSON
     * @param request Requisição HTTP
     * @return ResponseEntity com ErrorResponse
     */
    @ExceptionHandler(JsonParsingException.class)
    public ResponseEntity<ErrorResponse> handleJsonParsingException(
            JsonParsingException exception,
            HttpServletRequest request) {
        
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Erro de parsing JSON")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Trata exceções genéricas não capturadas.
     * 
     * @param exception Exceção genérica
     * @param request Requisição web
     * @return ResponseEntity com ErrorResponse
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception exception,
            WebRequest request) {
        
        String path = "";
        if (request instanceof ServletWebRequest) {
            path = ((ServletWebRequest) request).getRequest().getRequestURI();
        }
        
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Erro interno do servidor")
                .message(exception.getMessage())
                .path(path)
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
