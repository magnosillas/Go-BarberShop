package br.edu.ufape.gobarber.controller.auth;

import br.edu.ufape.gobarber.dto.user.LoginDTO;
import br.edu.ufape.gobarber.model.login.User;
import br.edu.ufape.gobarber.security.TokenService;
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
public class AuthController {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    
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

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        tokenService.invalidateToken(request);

        return ResponseEntity.ok().build();
    }

}