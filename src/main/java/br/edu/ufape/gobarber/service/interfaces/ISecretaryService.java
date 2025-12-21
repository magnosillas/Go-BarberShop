package br.edu.ufape.gobarber.service.interfaces;

import br.edu.ufape.gobarber.controller.request.SecretaryRequest;
import br.edu.ufape.gobarber.controller.response.PageResponse;
import br.edu.ufape.gobarber.controller.response.SecretaryResponse;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Interface de serviço para operações de Secretary.
 * Segue o padrão da arquitetura base (Interface + Impl).
 * 
 * @author GoBarber Team
 * @version 2.0
 */
public interface ISecretaryService {

    /**
     * Cria uma nova secretária.
     * 
     * @param request Dados da secretária
     * @param profilePhoto Foto de perfil (opcional)
     * @return SecretaryResponse criado
     * @throws DataBaseException se houver erro no banco de dados
     * @throws IOException se houver erro ao processar a foto
     */
    SecretaryResponse createSecretary(SecretaryRequest request, MultipartFile profilePhoto) throws DataBaseException, IOException;

    /**
     * Atualiza uma secretária existente.
     * 
     * @param id ID da secretária
     * @param request Novos dados da secretária
     * @param profilePhoto Nova foto de perfil (opcional)
     * @return SecretaryResponse atualizado
     * @throws DataBaseException se a secretária não for encontrada
     * @throws IOException se houver erro ao processar a foto
     */
    SecretaryResponse updateSecretary(Integer id, SecretaryRequest request, MultipartFile profilePhoto) throws DataBaseException, IOException;

    /**
     * Remove uma secretária.
     * 
     * @param id ID da secretária
     * @throws DataBaseException se a secretária não for encontrada
     */
    void deleteSecretary(Integer id) throws DataBaseException;

    /**
     * Busca uma secretária por ID.
     * 
     * @param id ID da secretária
     * @return SecretaryResponse encontrada
     * @throws DataBaseException se a secretária não for encontrada
     */
    SecretaryResponse getSecretaryById(Integer id) throws DataBaseException;

    /**
     * Lista todas as secretárias com paginação.
     * 
     * @param page Número da página
     * @param size Tamanho da página
     * @return Página de secretárias
     */
    PageResponse<SecretaryResponse> getAllSecretaries(Integer page, Integer size);

    /**
     * Obtém foto de perfil de uma secretária.
     * 
     * @param id ID da secretária
     * @return Bytes da foto de perfil
     * @throws DataBaseException se a secretária não for encontrada
     */
    byte[] getProfilePhoto(Integer id) throws DataBaseException;
}
