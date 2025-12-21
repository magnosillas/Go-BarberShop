package br.edu.ufape.gobarber.doc;

import br.edu.ufape.gobarber.controller.response.AddressResponse;
import br.edu.ufape.gobarber.controller.response.PageResponse;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Address;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Documentação Swagger para AddressController.
 * Define operações de CRUD para endereços.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Tag(name = "Endereços", description = "Operações de gerenciamento de endereços")
public interface AddressControllerDoc {

    @Operation(summary = "Criar endereço", description = "Cria um novo endereço no sistema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Endereço criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Address.class)
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
                                              "message": "Um ou mais campos estão inválidos",
                                              "details": {
                                                "street": "Rua é obrigatória"
                                              }
                                            }"""
                            )
                    )
            )
    })
    @PostMapping
    ResponseEntity<Address> createAddress(@Valid @RequestBody Object address) throws DataBaseException;

    @Operation(summary = "Atualizar endereço", description = "Atualiza um endereço existente")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Endereço atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Address.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Endereço não encontrado"
            )
    })
    @PutMapping("/{id}")
    ResponseEntity<Address> updateAddress(@PathVariable Integer id, @RequestBody Address addressDetails) throws DataBaseException;

    @Operation(summary = "Remover endereço", description = "Remove um endereço do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteAddress(@PathVariable Integer id) throws DataBaseException;

    @Operation(summary = "Buscar endereço por ID", description = "Retorna um endereço específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Endereço encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Address.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    @GetMapping("/{id}")
    ResponseEntity<Address> getAddressById(@PathVariable Integer id) throws DataBaseException;

    @Operation(summary = "Listar endereços", description = "Retorna todos os endereços cadastrados")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de endereços",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Address.class)
                    )
            )
    })
    @GetMapping
    ResponseEntity<List<Address>> getAllAddresses();
}
