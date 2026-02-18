package br.edu.ufape.gobarber.controller.advice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * DTO padronizado para respostas de erro da API.
 * Segue o padrão da arquitetura base do projeto Transporte.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    /**
     * Timestamp do momento em que o erro ocorreu
     */
    private Instant timestamp;
    
    /**
     * Código HTTP do status da resposta
     */
    private int status;
    
    /**
     * Tipo do erro (ex: "Erro de validação", "Recurso não encontrado")
     */
    private String error;
    
    /**
     * Mensagem descritiva do erro
     */
    private String message;
    
    /**
     * Path da requisição que gerou o erro
     */
    private String path;
    
    /**
     * Detalhes adicionais do erro (usado principalmente em validações)
     * Mapa de campo -> mensagem de erro
     */
    private Map<String, String> details;
}
