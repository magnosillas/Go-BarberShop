package br.edu.ufape.gobarber.service.interfaces;

import br.edu.ufape.gobarber.controller.request.ProductRequest;
import br.edu.ufape.gobarber.controller.response.PageResponse;
import br.edu.ufape.gobarber.controller.response.ProductResponse;
import br.edu.ufape.gobarber.exceptions.DataBaseException;

/**
 * Interface de serviço para operações de Product.
 * Segue o padrão da arquitetura base (Interface + Impl).
 * 
 * @author GoBarber Team
 * @version 2.0
 */
public interface IProductService {

    /**
     * Cria um novo produto.
     * 
     * @param request Dados do produto
     * @return ProductResponse criado
     * @throws DataBaseException se houver erro no banco de dados
     */
    ProductResponse createProduct(ProductRequest request) throws DataBaseException;

    /**
     * Atualiza um produto existente.
     * 
     * @param id ID do produto
     * @param request Novos dados do produto
     * @return ProductResponse atualizado
     * @throws DataBaseException se o produto não for encontrado
     */
    ProductResponse updateProduct(Integer id, ProductRequest request) throws DataBaseException;

    /**
     * Remove um produto.
     * 
     * @param id ID do produto
     * @throws DataBaseException se o produto não for encontrado
     */
    void deleteProduct(Integer id) throws DataBaseException;

    /**
     * Busca um produto por ID.
     * 
     * @param id ID do produto
     * @return ProductResponse encontrado
     * @throws DataBaseException se o produto não for encontrado
     */
    ProductResponse getProductById(Integer id) throws DataBaseException;

    /**
     * Lista todos os produtos com paginação.
     * 
     * @param page Número da página
     * @param size Tamanho da página
     * @return Página de produtos
     */
    PageResponse<ProductResponse> getAllProducts(Integer page, Integer size);

    /**
     * Busca produto por nome.
     * 
     * @param name Nome do produto
     * @return ProductResponse encontrado
     * @throws DataBaseException se o produto não for encontrado
     */
    ProductResponse getProductByName(String name) throws DataBaseException;
}
