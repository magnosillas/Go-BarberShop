package br.edu.ufape.gobarber.controller.response;

import br.edu.ufape.gobarber.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para endereços.
 * Segue o padrão da arquitetura base com construtor que recebe Model.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {

    private Integer idAddress;
    private String street;
    private Integer number;
    private String neighborhood;
    private String city;
    private String state;
    private String cep;

    /**
     * Construtor que converte uma entidade Address para o DTO de resposta.
     * 
     * @param address Entidade Address a ser convertida
     */
    public AddressResponse(Address address) {
        this.idAddress = address.getIdAddress();
        this.street = address.getStreet();
        this.number = address.getNumber();
        this.neighborhood = address.getNeighborhood();
        this.city = address.getCity();
        this.state = address.getState();
        this.cep = address.getCep();
    }
}
