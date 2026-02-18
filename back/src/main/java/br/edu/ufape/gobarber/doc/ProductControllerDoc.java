package br.edu.ufape.gobarber.doc;

import br.edu.ufape.gobarber.dto.page.PageProductDTO;
import br.edu.ufape.gobarber.dto.product.ProductDTO;
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
 * Documentação Swagger para ProductController.
 * Define operações de gerenciamento de produtos.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Tag(name = "Produtos", description = "Operações de gerenciamento de produtos")
public interface ProductControllerDoc {

    @Operation(summary = "Criar produto", description = "Cria um novo produto no sistema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Produto criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    @PostMapping
    ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody Object product) throws DataBaseException;

    @Operation(summary = "Atualizar produto", description = "Atualiza um produto existente")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PutMapping("/{id}")
    ResponseEntity<ProductDTO> updateProduct(@PathVariable Integer id, @Valid @RequestBody Object product) throws DataBaseException;

    @Operation(summary = "Remover produto", description = "Remove um produto do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteProduct(@PathVariable Integer id) throws DataBaseException;

    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto específico")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{id}")
    ResponseEntity<ProductDTO> getProductById(@PathVariable Integer id) throws DataBaseException;

    @Operation(summary = "Listar produtos", description = "Retorna todos os produtos com paginação")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de produtos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageProductDTO.class)
                    )
            )
    })
    @GetMapping
    ResponseEntity<PageProductDTO> getAllProducts(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size);

    @Operation(summary = "Buscar produto por nome", description = "Retorna um produto pelo nome")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/name/{name}")
    ResponseEntity<ProductDTO> getProductByName(@PathVariable String name) throws DataBaseException;
}
