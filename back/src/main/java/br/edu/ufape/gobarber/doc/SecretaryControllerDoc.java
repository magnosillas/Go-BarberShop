package br.edu.ufape.gobarber.doc;

import br.edu.ufape.gobarber.dto.page.PageSecretaryDTO;
import br.edu.ufape.gobarber.dto.secretary.SecretaryDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * Documentação Swagger para SecretaryController.
 * Define operações de gerenciamento de secretárias.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Tag(name = "Secretárias", description = "Operações de gerenciamento de secretárias")
public interface SecretaryControllerDoc {

    @Operation(
            summary = "Criar secretária",
            description = "Cria uma nova secretária no sistema. Aceita dados em multipart/form-data incluindo foto."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Secretária criada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SecretaryDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "409", description = "Secretária já existe (conflito)")
    })
    @PostMapping(consumes = "multipart/form-data")
    ResponseEntity<SecretaryDTO> createSecretary(
            @Valid @RequestPart("secretary") Object secretary,
            @RequestPart(value = "photo", required = false) MultipartFile photo) throws DataBaseException;

    @Operation(
            summary = "Atualizar secretária",
            description = "Atualiza uma secretária existente. Aceita dados em multipart/form-data incluindo nova foto."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Secretária atualizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SecretaryDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Secretária não encontrada")
    })
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    ResponseEntity<SecretaryDTO> updateSecretary(
            @PathVariable Integer id,
            @Valid @RequestPart("secretary") Object secretary,
            @RequestPart(value = "photo", required = false) MultipartFile photo) throws DataBaseException;

    @Operation(summary = "Remover secretária", description = "Remove uma secretária do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Secretária removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Secretária não encontrada")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteSecretary(@PathVariable Integer id) throws DataBaseException;

    @Operation(summary = "Buscar secretária por ID", description = "Retorna uma secretária específica")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Secretária encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SecretaryDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Secretária não encontrada")
    })
    @GetMapping("/{id}")
    ResponseEntity<SecretaryDTO> getSecretaryById(@PathVariable Integer id) throws DataBaseException;

    @Operation(summary = "Listar secretárias", description = "Retorna todas as secretárias com paginação")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de secretárias",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageSecretaryDTO.class)
                    )
            )
    })
    @GetMapping
    ResponseEntity<PageSecretaryDTO> getAllSecretaries(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size);

    @Operation(summary = "Obter foto da secretária", description = "Retorna a foto de perfil da secretária")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Foto encontrada",
                    content = @Content(mediaType = "image/*")
            ),
            @ApiResponse(responseCode = "404", description = "Secretária ou foto não encontrada")
    })
    @GetMapping("/{id}/photo")
    ResponseEntity<byte[]> getSecretaryPhoto(@PathVariable Integer id) throws DataBaseException;

    @Operation(summary = "Buscar secretária por nome", description = "Retorna uma secretária pelo nome")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Secretária encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SecretaryDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Secretária não encontrada")
    })
    @GetMapping("/name/{name}")
    ResponseEntity<SecretaryDTO> getSecretaryByName(@PathVariable String name) throws DataBaseException;
}
