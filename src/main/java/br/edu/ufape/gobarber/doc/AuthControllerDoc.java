package br.edu.ufape.gobarber.doc;

import br.edu.ufape.gobarber.dto.user.LoginDTO;
import br.edu.ufape.gobarber.dto.user.UserCreateDTO;
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

    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria um novo usuário no sistema com a role especificada (ADMIN, BARBER, SECRETARY)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário registrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "idUsuario": 1,
                                                "login": "usuario@email.com"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email já cadastrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "error": "Usuário já cadastrado com esse email."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Role inválida",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "error": "Role não encontrada"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody @Valid UserCreateDTO userCreateDTO);
}
