package br.edu.ufape.gobarber.doc;

import br.edu.ufape.gobarber.dto.services.ServicesCreateDTO;
import br.edu.ufape.gobarber.dto.services.ServicesDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Documentação Swagger para ServicesController.
 * Define operações de gerenciamento de serviços oferecidos pela barbearia.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Tag(name = "Serviços", description = "Operações de gerenciamento de serviços da barbearia")
public interface ServicesControllerDoc {

    @Operation(summary = "Criar serviço", description = "Cria um novo serviço no sistema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Serviço criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServicesDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    @PostMapping
    ResponseEntity<ServicesDTO> createService(@Valid @RequestBody ServicesCreateDTO services) throws DataBaseException;

    @Operation(summary = "Atualizar serviço", description = "Atualiza um serviço existente")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Serviço atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServicesDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    })
    @PutMapping("/{id}")
    ResponseEntity<ServicesDTO> updateService(@PathVariable Integer id, @Valid @RequestBody ServicesCreateDTO services) throws DataBaseException;

    @Operation(summary = "Remover serviço", description = "Remove um serviço do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Serviço removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteService(@PathVariable Integer id) throws DataBaseException;

    @Operation(summary = "Buscar serviço por ID", description = "Retorna um serviço específico")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Serviço encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServicesDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    })
    @GetMapping("/{id}")
    ResponseEntity<ServicesDTO> getServiceById(@PathVariable Integer id) throws DataBaseException;

    @Operation(summary = "Listar todos os serviços", description = "Retorna todos os serviços cadastrados")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de serviços",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServicesDTO.class)
                    )
            )
    })
    @GetMapping
    ResponseEntity<List<ServicesDTO>> getAllServices();

    @Operation(summary = "Buscar serviço por nome", description = "Retorna um serviço pelo nome")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Serviço encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServicesDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    })
    @GetMapping("/name/{name}")
    ResponseEntity<ServicesDTO> getServiceByName(@PathVariable String name) throws DataBaseException;

    @Operation(summary = "Listar serviços por barbeiro", description = "Retorna todos os serviços de um barbeiro específico")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de serviços do barbeiro",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServicesDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Barbeiro não encontrado")
    })
    @GetMapping("/barber/{barberId}")
    ResponseEntity<List<ServicesDTO>> getServicesByBarberId(@PathVariable Integer barberId) throws DataBaseException;
}
