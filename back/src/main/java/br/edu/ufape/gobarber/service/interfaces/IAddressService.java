package br.edu.ufape.gobarber.service.interfaces;

import br.edu.ufape.gobarber.controller.request.AddressRequest;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Address;

import java.util.List;

/**
 * Interface de serviço para operações de Address.
 * Segue o padrão da arquitetura base (Interface + Impl).
 * 
 * @author GoBarber Team
 * @version 2.0
 */
public interface IAddressService {

    /**
     * Cria um novo endereço.
     * 
     * @param request Dados do endereço
     * @return Address criado
     * @throws DataBaseException se houver erro no banco de dados
     */
    Address createAddress(AddressRequest request) throws DataBaseException;

    /**
     * Atualiza um endereço existente.
     * 
     * @param id ID do endereço
     * @param request Novos dados do endereço
     * @return Address atualizado
     * @throws DataBaseException se o endereço não for encontrado
     */
    Address updateAddress(Integer id, AddressRequest request) throws DataBaseException;

    /**
     * Remove um endereço.
     * 
     * @param id ID do endereço
     * @throws DataBaseException se o endereço não for encontrado
     */
    void deleteAddress(Integer id) throws DataBaseException;

    /**
     * Busca um endereço por ID.
     * 
     * @param id ID do endereço
     * @return Address encontrado
     * @throws DataBaseException se o endereço não for encontrado
     */
    Address getAddressById(Integer id) throws DataBaseException;

    /**
     * Lista todos os endereços.
     * 
     * @return Lista de endereços
     */
    List<Address> getAllAddresses();
}
