package br.edu.ufape.gobarber.service.interfaces;

import br.edu.ufape.gobarber.controller.request.BarberRequest;
import br.edu.ufape.gobarber.controller.response.BarberResponse;
import br.edu.ufape.gobarber.controller.response.PageResponse;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Interface de serviço para operações de Barber.
 * Segue o padrão da arquitetura base (Interface + Impl).
 * 
 * @author GoBarber Team
 * @version 2.0
 */
public interface IBarberService {

    /**
     * Cria um novo barbeiro.
     * 
     * @param request Dados do barbeiro
     * @param profilePhoto Foto de perfil (opcional)
     * @return BarberResponse criado
     * @throws DataBaseException se houver erro no banco de dados
     * @throws IOException se houver erro ao processar a foto
     */
    BarberResponse createBarber(BarberRequest request, MultipartFile profilePhoto) throws DataBaseException, IOException;

    /**
     * Atualiza um barbeiro existente.
     * 
     * @param id ID do barbeiro
     * @param request Novos dados do barbeiro
     * @param profilePhoto Nova foto de perfil (opcional)
     * @return BarberResponse atualizado
     * @throws DataBaseException se o barbeiro não for encontrado
     * @throws IOException se houver erro ao processar a foto
     */
    BarberResponse updateBarber(Integer id, BarberRequest request, MultipartFile profilePhoto) throws DataBaseException, IOException;

    /**
     * Remove um barbeiro.
     * 
     * @param id ID do barbeiro
     * @throws DataBaseException se o barbeiro não for encontrado
     */
    void deleteBarber(Integer id) throws DataBaseException;

    /**
     * Busca um barbeiro por ID.
     * 
     * @param id ID do barbeiro
     * @return BarberResponse encontrado
     * @throws DataBaseException se o barbeiro não for encontrado
     */
    BarberResponse getBarberById(Integer id) throws DataBaseException;

    /**
     * Lista todos os barbeiros com paginação.
     * 
     * @param page Número da página
     * @param size Tamanho da página
     * @return Página de barbeiros
     */
    PageResponse<BarberResponse> getAllBarbers(Integer page, Integer size);

    /**
     * Obtém foto de perfil de um barbeiro.
     * 
     * @param id ID do barbeiro
     * @return Bytes da foto de perfil
     * @throws DataBaseException se o barbeiro não for encontrado
     */
    byte[] getProfilePhoto(Integer id) throws DataBaseException;

    /**
     * Adiciona um serviço a um barbeiro.
     * 
     * @param barberId ID do barbeiro
     * @param serviceId ID do serviço
     * @return BarberResponse atualizado
     * @throws DataBaseException se o barbeiro ou serviço não for encontrado
     */
    BarberResponse addServiceToBarber(Integer barberId, Integer serviceId) throws DataBaseException;

    /**
     * Remove um serviço de um barbeiro.
     * 
     * @param barberId ID do barbeiro
     * @param serviceId ID do serviço
     * @return BarberResponse atualizado
     * @throws DataBaseException se o barbeiro ou serviço não for encontrado
     */
    BarberResponse removeServiceFromBarber(Integer barberId, Integer serviceId) throws DataBaseException;
}
