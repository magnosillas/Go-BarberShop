package br.edu.ufape.gobarber.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para login/autenticação.
 * Segue o padrão da arquitetura base.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String token;
    private String role;
    private String message;

    /**
     * Construtor para resposta de login bem-sucedido.
     * 
     * @param token Token JWT gerado
     * @param role Role do usuário autenticado
     */
    public LoginResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }

    /**
     * Cria uma resposta de erro de autenticação.
     * 
     * @param message Mensagem de erro
     * @return LoginResponse com mensagem de erro
     */
    public static LoginResponse error(String message) {
        return LoginResponse.builder()
                .message(message)
                .build();
    }
}
