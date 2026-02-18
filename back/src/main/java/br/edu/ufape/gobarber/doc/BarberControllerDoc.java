package br.edu.ufape.gobarber.doc;

import br.edu.ufape.gobarber.dto.barber.BarberDTO;
import br.edu.ufape.gobarber.dto.page.PageBarberDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Documentação Swagger para BarberController.
 * Define operações de gerenciamento de barbeiros.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Tag(name = "Barbeiros", description = "Operações de gerenciamento de barbeiros")
public interface BarberControllerDoc {

    @Operation(summary = "Criar barbeiro", description = "Cria um novo barbeiro com foto de perfil opcional")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Barbeiro criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BarberDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2025-12-20T10:30:00Z",
                                              "status": 400,
                                              "error": "Erro de validação",
                                              "message": "Um ou mais campos estão inválidos"
                                            }"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "CPF ou email já cadastrado"
            )
    })
    @PostMapping(consumes = "multipart/form-data")
    ResponseEntity<BarberDTO> createBarber(
            @RequestPart("barber") String barberJson,
            @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto) throws DataBaseException, IOException;

    @Operation(summary = "Atualizar barbeiro", description = "Atualiza um barbeiro existente")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Barbeiro atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BarberDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Barbeiro não encontrado")
    })
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    ResponseEntity<BarberDTO> updateBarber(
            @PathVariable Integer id,
            @RequestPart("barber") String barberJson,
            @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto) throws DataBaseException, IOException;

    @Operation(summary = "Remover barbeiro", description = "Remove um barbeiro do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Barbeiro removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Barbeiro não encontrado")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteBarber(@PathVariable Integer id) throws DataBaseException;

    @Operation(summary = "Buscar barbeiro por ID", description = "Retorna um barbeiro específico")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Barbeiro encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BarberDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Barbeiro não encontrado")
    })
    @GetMapping("/{id}")
    ResponseEntity<BarberDTO> getBarberById(@PathVariable Integer id) throws DataBaseException;

    @Operation(summary = "Listar barbeiros", description = "Retorna todos os barbeiros com paginação")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de barbeiros",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageBarberDTO.class)
                    )
            )
    })
    @GetMapping
    ResponseEntity<PageBarberDTO> getAllBarbers(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size);

    @Operation(summary = "Obter foto de perfil", description = "Retorna a foto de perfil de um barbeiro")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Foto de perfil",
                    content = @Content(mediaType = "image/jpeg")
            ),
            @ApiResponse(responseCode = "404", description = "Foto não encontrada")
    })
    @GetMapping("/{id}/profile-photo")
    ResponseEntity<byte[]> getProfilePhoto(@PathVariable Integer id) throws DataBaseException;

    @Operation(summary = "Adicionar serviço ao barbeiro", description = "Associa um serviço a um barbeiro")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Serviço adicionado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BarberDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Barbeiro ou serviço não encontrado")
    })
    @PostMapping("/{barberId}/services/{serviceId}")
    ResponseEntity<BarberDTO> addServiceToBarber(
            @PathVariable Integer barberId,
            @PathVariable Integer serviceId) throws DataBaseException;

    @Operation(summary = "Remover serviço do barbeiro", description = "Remove associação de um serviço a um barbeiro")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Serviço removido com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BarberDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Barbeiro ou serviço não encontrado")
    })
    @DeleteMapping("/{barberId}/services/{serviceId}")
    ResponseEntity<BarberDTO> removeServiceFromBarber(
            @PathVariable Integer barberId,
            @PathVariable Integer serviceId) throws DataBaseException;
}
