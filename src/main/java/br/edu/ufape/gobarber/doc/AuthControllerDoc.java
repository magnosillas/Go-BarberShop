package br.edu.ufape.gobarber.doc;

import br.edu.ufape.gobarber.dto.user.LoginDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * Documentação Swagger para AuthController.
 * Define operações de autenticação e autorização.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Tag(name = "Autenticação", description = "Operações de autenticação e autorização")
public interface AuthControllerDoc {

    @Operation(
            summary = "Autenticar usuário",
            description = "Realiza a autenticação do usuário e retorna um token JWT válido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "Autenticação realizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "role": "ADMIN",
                                                "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Email ou senha inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "error": "Email ou senha inválidos"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Falha de autenticação",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "error": "Falha de Autenticação, Tente Novamente!"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    ResponseEntity<Map<String, String>> auth(@RequestBody @Valid LoginDTO loginDTO);

    @Operation(
            summary = "Logout",
            description = "Invalida o token JWT do usuário, encerrando a sessão"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Logout realizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token inválido ou não fornecido"
            )
    })
    @PostMapping("/logout")
    ResponseEntity<Void> logout(HttpServletRequest request);
}
