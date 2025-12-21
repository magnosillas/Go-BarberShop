package br.edu.ufape.gobarber.controller.request;

import br.edu.ufape.gobarber.model.Address;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * DTO de requisição para criação e atualização de endereços.
 * Segue o padrão da arquitetura base com método toModel().
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressRequest {

    @NotBlank(message = "Rua é obrigatória")
    private String street;
    
    private Integer number;

    @NotBlank(message = "Bairro é obrigatório")
    private String neighborhood;

    @NotBlank(message = "Cidade é obrigatória")
    private String city;

    @NotBlank(message = "Estado é obrigatório")
    private String state;
    
    private String cep;

    /**
     * Converte o DTO de requisição para a entidade Address.
     * 
     * @return Nova instância de Address com os dados do request
     */
    public Address toModel() {
        Address address = new Address();
        address.setStreet(this.street);
        address.setNumber(this.number);
        address.setNeighborhood(this.neighborhood);
        address.setCity(this.city);
        address.setState(this.state);
        address.setCep(this.cep);
        return address;
    }

    /**
     * Atualiza uma entidade Address existente com os dados do request.
     * 
     * @param address Entidade existente a ser atualizada
     */
    public void updateModel(Address address) {
        address.setStreet(this.street);
        address.setNumber(this.number);
        address.setNeighborhood(this.neighborhood);
        address.setCity(this.city);
        address.setState(this.state);
        address.setCep(this.cep);
    }
}
