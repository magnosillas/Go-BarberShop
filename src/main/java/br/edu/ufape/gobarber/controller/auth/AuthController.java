package br.edu.ufape.gobarber.controller.auth;

import br.edu.ufape.gobarber.doc.AuthControllerDoc;
import br.edu.ufape.gobarber.dto.user.LoginDTO;
import br.edu.ufape.gobarber.dto.user.UserCreateDTO;
import br.edu.ufape.gobarber.dto.user.UserDTO;
import br.edu.ufape.gobarber.exceptions.InvalidRoleException;
import br.edu.ufape.gobarber.exceptions.UniqueConstraintViolationException;
import br.edu.ufape.gobarber.model.login.User;
import br.edu.ufape.gobarber.security.TokenService;
import br.edu.ufape.gobarber.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthControllerDoc {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    
    @Override
    @PostMapping
    public ResponseEntity<Map<String, String>> auth(@RequestBody @Valid LoginDTO loginDTO) {
        try {
            log.info("Tentativa de login para: {}", loginDTO.getLogin());
            
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getLogin(),
                            loginDTO.getPassword()
                    );

            Authentication authentication =
                    authenticationManager.authenticate(
                            usernamePasswordAuthenticationToken);

            User validatedUser = (User) authentication.getPrincipal();
            log.info("Usuário autenticado: {}", validatedUser.getLogin());

            String jwt = tokenService.gerarTokenJwt(validatedUser);
            String role = validatedUser.getRole().getAuthority();
            role = role.replace(role.substring(0, 5), "");

            Map<String, String> response = new HashMap<>();
            response.put("role", role);
            response.put("token", jwt);

            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

        }catch (BadCredentialsException e) {
            log.warn("Credenciais inválidas para: {}", loginDTO.getLogin());
            return new ResponseEntity<>(Map.of("error", "Email ou senha inválidos"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Erro de autenticação: {} - {}", e.getClass().getSimpleName(), e.getMessage());
            return new ResponseEntity<>(Map.of("error", "Falha de Autenticação, Tente Novamente!"), HttpStatus.FORBIDDEN);
        }
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        tokenService.invalidateToken(request);

        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        try {
            log.info("Tentativa de registro para: {}", userCreateDTO.getLogin());
            UserDTO user = userService.create(userCreateDTO);
            log.info("Usuário registrado com sucesso: {}", user.getLogin());
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (UniqueConstraintViolationException e) {
            log.warn("Email já cadastrado: {}", userCreateDTO.getLogin());
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.CONFLICT);
        } catch (InvalidRoleException e) {
            log.warn("Role inválida: {}", userCreateDTO.getRole());
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Erro ao registrar usuário: {} - {}", e.getClass().getSimpleName(), e.getMessage());
            return new ResponseEntity<>(Map.of("error", "Erro ao registrar usuário"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}