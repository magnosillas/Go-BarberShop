package br.edu.ufape.gobarber.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * DTO de requisição para criação e atualização de secretárias.
 * Segue o padrão da arquitetura base.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecretaryRequest {

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;
    
    @NotBlank(message = "Senha é obrigatória")
    private String password;
    
    @NotBlank(message = "Nome é obrigatório")
    private String name;
    
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;
    
    private String contact;
    
    @NotBlank(message = "Horário de início é obrigatório")
    private String start;
    
    @NotBlank(message = "Horário de término é obrigatório")
    private String end;
    
    @Valid
    private AddressRequest address;
    
    @Positive(message = "Salário deve ser positivo")
    private double salary;
    
    @NotNull(message = "Data de admissão é obrigatória")
    private LocalDate admissionDate;
    
    private Integer workload;
}
