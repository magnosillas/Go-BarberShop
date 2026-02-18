package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.dto.client.ClientCreateDTO;
import br.edu.ufape.gobarber.dto.user.UserCreateDTO;
import br.edu.ufape.gobarber.exceptions.InvalidRoleException;
import br.edu.ufape.gobarber.exceptions.UniqueConstraintViolationException;
import br.edu.ufape.gobarber.model.login.User;
import br.edu.ufape.gobarber.security.TokenService;
import br.edu.ufape.gobarber.service.ClientService;
import br.edu.ufape.gobarber.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
@Slf4j
public class PublicController {

    private final UserService userService;
    private final ClientService clientService;
    private final TokenService tokenService;

    /**
     * Registro público de cliente.
     * Recebe name, email, phone, password.
     * Cria um User com role CLIENT e um perfil de Client associado.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerClient(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String email = body.get("email");
        String phone = body.get("phone");
        String password = body.get("password");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return new ResponseEntity<>(Map.of("error", "Email e senha são obrigatórios"), HttpStatus.BAD_REQUEST);
        }

        log.info("Registro público de cliente: {}", email);

        // 1. Criar o User com role CLIENT
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setLogin(email);
        userCreateDTO.setPassword(password);
        userCreateDTO.setRole("CLIENT");

        try {
            userService.create(userCreateDTO);
        } catch (UniqueConstraintViolationException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.CONFLICT);
        } catch (InvalidRoleException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        // 2. Criar o perfil de Client
        try {
            ClientCreateDTO clientDTO = new ClientCreateDTO();
            clientDTO.setName(name != null && !name.isBlank() ? name : email);
            clientDTO.setEmail(email);
            clientDTO.setPhone(phone != null ? phone.replaceAll("\\D", "") : "0000000000");
            clientDTO.setPassword(password);
            clientService.createClient(clientDTO, null);
        } catch (Exception e) {
            log.warn("Perfil de cliente não pôde ser criado (usuário foi criado): {}", e.getMessage());
        }

        // 3. Auto-login: gerar token
        try {
            java.util.Optional<User> userOpt = userService.findByLogin(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String jwt = tokenService.gerarTokenJwt(user);
                String role = user.getRole().getAuthority().replace("ROLE_", "");

                Map<String, String> response = new HashMap<>();
                response.put("token", jwt);
                response.put("role", role);
                response.put("name", name != null ? name : "");

                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            log.warn("Auto-login falhou após registro: {}", e.getMessage());
        }

        return new ResponseEntity<>(Map.of("message", "Conta criada com sucesso"), HttpStatus.CREATED);
    }
}
