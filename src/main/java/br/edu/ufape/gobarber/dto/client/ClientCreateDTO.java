package br.edu.ufape.gobarber.dto.client;

import br.edu.ufape.gobarber.dto.address.AddressCreateDTO;
import br.edu.ufape.gobarber.model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientCreateDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
    private String phone;

    @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos")
    private String cpf;

    private LocalDate birthDate;

    private Client.Gender gender;

    @Valid
    private AddressCreateDTO address;

    private String notes;

    private Integer preferredBarberId;

    private Client.ContactMethod preferredContactMethod;

    private Boolean receivePromotions;

    private Boolean receiveReminders;

    // Credenciais opcionais para login
    private String password;
}
