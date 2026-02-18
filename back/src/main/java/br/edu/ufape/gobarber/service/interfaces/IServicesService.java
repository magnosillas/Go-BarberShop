package br.edu.ufape.gobarber.service.interfaces;

import br.edu.ufape.gobarber.controller.request.ServicesRequest;
import br.edu.ufape.gobarber.controller.response.PageResponse;
import br.edu.ufape.gobarber.controller.response.ServicesResponse;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.exceptions.ServiceAlreadyExistsException;

/**
 * Interface de serviço para operações de Services.
 * Segue o padrão da arquitetura base (Interface + Impl).
 * 
 * @author GoBarber Team
 * @version 2.0
 */
public interface IServicesService {

    /**
     * Cria um novo serviço.
     * 
     * @param request Dados do serviço
     * @return ServicesResponse criado
     * @throws ServiceAlreadyExistsException se já existir um serviço com o mesmo nome
     */
    ServicesResponse createService(ServicesRequest request) throws ServiceAlreadyExistsException;

    /**
     * Atualiza um serviço existente.
     * 
     * @param id ID do serviço
     * @param request Novos dados do serviço
     * @return ServicesResponse atualizado
     * @throws DataBaseException se o serviço não for encontrado
     */
    ServicesResponse updateService(Integer id, ServicesRequest request) throws DataBaseException;

    /**
     * Remove um serviço.
     * 
     * @param id ID do serviço
     * @throws DataBaseException se o serviço não for encontrado
     */
    void deleteService(Integer id) throws DataBaseException;

    /**
     * Busca um serviço por ID.
     * 
     * @param id ID do serviço
     * @return ServicesResponse encontrado
     * @throws DataBaseException se o serviço não for encontrado
     */
    ServicesResponse getServiceById(Integer id) throws DataBaseException;

    /**
     * Lista todos os serviços com paginação.
     * 
     * @param page Número da página
     * @param size Tamanho da página
     * @return Página de serviços
     */
    PageResponse<ServicesResponse> getAllServices(Integer page, Integer size);
}
