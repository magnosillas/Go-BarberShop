package br.edu.ufape.gobarber.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * DTO de requisição para login/autenticação.
 * Segue o padrão da arquitetura base.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String login;
    
    @NotBlank(message = "Senha é obrigatória")
    private String password;
}
