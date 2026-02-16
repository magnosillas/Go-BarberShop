package br.edu.ufape.gobarber.service.interfaces;

import br.edu.ufape.gobarber.controller.request.ProductStockRequest;
import br.edu.ufape.gobarber.controller.response.PageResponse;
import br.edu.ufape.gobarber.controller.response.ProductStockResponse;
import br.edu.ufape.gobarber.exceptions.DataBaseException;

import java.util.List;

/**
 * Interface de serviço para operações de ProductStock.
 * Segue o padrão da arquitetura base (Interface + Impl).
 * 
 * @author GoBarber Team
 * @version 2.0
 */
public interface IProductStockService {

    /**
     * Cria um novo item de estoque.
     * 
     * @param request Dados do estoque
     * @return ProductStockResponse criado
     * @throws DataBaseException se houver erro no banco de dados
     */
    ProductStockResponse createProductStock(ProductStockRequest request) throws DataBaseException;

    /**
     * Atualiza um item de estoque existente.
     * 
     * @param id ID do item de estoque
     * @param request Novos dados do estoque
     * @return ProductStockResponse atualizado
     * @throws DataBaseException se o item não for encontrado
     */
    ProductStockResponse updateProductStock(Integer id, ProductStockRequest request) throws DataBaseException;

    /**
     * Remove um item de estoque.
     * 
     * @param id ID do item de estoque
     * @throws DataBaseException se o item não for encontrado
     */
    void deleteProductStock(Integer id) throws DataBaseException;

    /**
     * Busca um item de estoque por ID.
     * 
     * @param id ID do item de estoque
     * @return ProductStockResponse encontrado
     * @throws DataBaseException se o item não for encontrado
     */
    ProductStockResponse getProductStockById(Integer id) throws DataBaseException;

    /**
     * Lista todos os itens de estoque com paginação.
     * 
     * @param page Número da página
     * @param size Tamanho da página
     * @return Página de itens de estoque
     */
    PageResponse<ProductStockResponse> getAllProductStock(Integer page, Integer size);

    /**
     * Busca itens de estoque por produto.
     * 
     * @param productId ID do produto
     * @return Lista de itens de estoque do produto
     * @throws DataBaseException se o produto não for encontrado
     */
    List<ProductStockResponse> getProductStockByProduct(Integer productId) throws DataBaseException;

    /**
     * Atualiza quantidade de um item de estoque.
     * 
     * @param id ID do item de estoque
     * @param quantity Nova quantidade
     * @return ProductStockResponse atualizado
     * @throws DataBaseException se o item não for encontrado
     */
    ProductStockResponse updateQuantity(Integer id, Integer quantity) throws DataBaseException;
}
