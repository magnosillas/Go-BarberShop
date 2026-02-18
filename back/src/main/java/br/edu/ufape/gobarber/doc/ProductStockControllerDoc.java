package br.edu.ufape.gobarber.doc;

import br.edu.ufape.gobarber.dto.page.PageStockDTO;
import br.edu.ufape.gobarber.dto.productStock.ProductStockDTO;
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

/**
 * Documentação Swagger para ProductStockController.
 * Define operações de gerenciamento de estoque de produtos.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Tag(name = "Estoque de Produtos", description = "Operações de gerenciamento de estoque")
public interface ProductStockControllerDoc {

    @Operation(summary = "Criar item de estoque", description = "Adiciona um novo item ao estoque")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Item de estoque criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductStockDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    @PostMapping
    ResponseEntity<ProductStockDTO> createProductStock(@Valid @RequestBody Object productStock) throws DataBaseException;

    @Operation(summary = "Atualizar item de estoque", description = "Atualiza um item de estoque existente")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Item de estoque atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductStockDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Item de estoque não encontrado")
    })
    @PutMapping("/{id}")
    ResponseEntity<ProductStockDTO> updateProductStock(@PathVariable Integer id, @Valid @RequestBody Object productStock) throws DataBaseException;

    @Operation(summary = "Remover item de estoque", description = "Remove um item do estoque")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item de estoque removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item de estoque não encontrado")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteProductStock(@PathVariable Integer id) throws DataBaseException;

    @Operation(summary = "Buscar item de estoque por ID", description = "Retorna um item de estoque específico")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Item de estoque encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductStockDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Item de estoque não encontrado")
    })
    @GetMapping("/{id}")
    ResponseEntity<ProductStockDTO> getProductStockById(@PathVariable Integer id) throws DataBaseException;

    @Operation(summary = "Listar itens de estoque", description = "Retorna todos os itens de estoque com paginação")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de itens de estoque",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageStockDTO.class)
                    )
            )
    })
    @GetMapping
    ResponseEntity<PageStockDTO> getAllProductStocks(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size);

    @Operation(summary = "Buscar estoque por produto", description = "Retorna o estoque de um produto específico")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estoque do produto encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductStockDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado no estoque")
    })
    @GetMapping("/product/{productId}")
    ResponseEntity<ProductStockDTO> getProductStockByProductId(@PathVariable Integer productId) throws DataBaseException;
}
