package br.edu.ufape.gobarber.service.interfaces;

import br.edu.ufape.gobarber.controller.request.SaleRequest;
import br.edu.ufape.gobarber.controller.response.PageResponse;
import br.edu.ufape.gobarber.controller.response.SaleResponse;
import br.edu.ufape.gobarber.exceptions.DataBaseConstraintException;
import br.edu.ufape.gobarber.exceptions.DataBaseException;

/**
 * Interface de serviço para operações de Sale.
 * Segue o padrão da arquitetura base (Interface + Impl).
 * 
 * @author GoBarber Team
 * @version 2.0
 */
public interface ISaleService {

    /**
     * Cria uma nova promoção.
     * 
     * @param request Dados da promoção
     * @return SaleResponse criado
     * @throws DataBaseException se houver erro no banco de dados
     * @throws DataBaseConstraintException se o cupom já existir
     */
    SaleResponse createSale(SaleRequest request) throws DataBaseException, DataBaseConstraintException;

    /**
     * Atualiza uma promoção existente.
     * 
     * @param id ID da promoção
     * @param request Novos dados da promoção
     * @return SaleResponse atualizado
     * @throws DataBaseException se a promoção não for encontrada
     */
    SaleResponse updateSale(Integer id, SaleRequest request) throws DataBaseException;

    /**
     * Remove uma promoção.
     * 
     * @param id ID da promoção
     * @throws DataBaseException se a promoção não for encontrada
     */
    void deleteSale(Integer id) throws DataBaseException;

    /**
     * Busca uma promoção por ID.
     * 
     * @param id ID da promoção
     * @return SaleResponse encontrado
     * @throws DataBaseException se a promoção não for encontrada
     */
    SaleResponse getSaleById(Integer id) throws DataBaseException;

    /**
     * Busca uma promoção por cupom.
     * 
     * @param coupon Código do cupom
     * @return SaleResponse encontrado
     * @throws DataBaseException se a promoção não for encontrada
     */
    SaleResponse getSaleByCoupon(String coupon) throws DataBaseException;

    /**
     * Lista todas as promoções com paginação.
     * 
     * @param page Número da página
     * @param size Tamanho da página
     * @return Página de promoções
     */
    PageResponse<SaleResponse> getAllSales(Integer page, Integer size);

    /**
     * Lista todas as promoções válidas (não expiradas) com paginação.
     * 
     * @param page Número da página
     * @param size Tamanho da página
     * @return Página de promoções válidas
     */
    PageResponse<SaleResponse> getAllValidSales(Integer page, Integer size);

    /**
     * Envia email de notificação sobre promoção.
     * 
     * @param saleId ID da promoção
     * @throws DataBaseException se a promoção não for encontrada
     */
    void sendSaleNotification(Integer saleId) throws DataBaseException;
}
