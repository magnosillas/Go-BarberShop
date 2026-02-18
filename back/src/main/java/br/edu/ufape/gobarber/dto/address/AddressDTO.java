package br.edu.ufape.gobarber.dto.address;

import br.edu.ufape.gobarber.model.Address;
import lombok.*;

/**
 * DTO para transferência de dados de endereço.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {

    private Integer idAddress;
    private String street;
    private Integer number;
    private String neighborhood;
    private String city;
    private String state;
    private String cep;

    /**
     * Converte uma entidade Address para AddressDTO.
     */
    public static AddressDTO fromEntity(Address address) {
        if (address == null) {
            return null;
        }
        return AddressDTO.builder()
                .idAddress(address.getIdAddress())
                .street(address.getStreet())
                .number(address.getNumber())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .cep(address.getCep())
                .build();
    }

    /**
     * Converte AddressDTO para entidade Address.
     */
    public Address toEntity() {
        Address address = new Address();
        address.setIdAddress(this.idAddress);
        address.setStreet(this.street);
        address.setNumber(this.number);
        address.setNeighborhood(this.neighborhood);
        address.setCity(this.city);
        address.setState(this.state);
        address.setCep(this.cep);
        return address;
    }

    /**
     * Atualiza uma entidade Address existente com dados do DTO.
     */
    public void updateEntity(Address address) {
        if (this.street != null) address.setStreet(this.street);
        if (this.number != null) address.setNumber(this.number);
        if (this.neighborhood != null) address.setNeighborhood(this.neighborhood);
        if (this.city != null) address.setCity(this.city);
        if (this.state != null) address.setState(this.state);
        if (this.cep != null) address.setCep(this.cep);
    }
}
