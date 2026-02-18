package br.edu.ufape.gobarber.dto.barbershop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BarbershopDTO {

    @Schema(description = "ID da barbearia")
    private Integer idBarbershop;

    @Schema(description = "Nome da barbearia", example = "Barbearia Central")
    private String name;

    @Schema(description = "Slug único para URL", example = "barbearia-central")
    private String slug;

    @Schema(description = "Descrição da barbearia")
    private String description;

    @Schema(description = "CNPJ da barbearia")
    private String cnpj;

    @Schema(description = "Telefone de contato")
    private String phone;

    @Schema(description = "Email de contato")
    private String email;

    @Schema(description = "URL do logotipo")
    private String logoUrl;

    @Schema(description = "URL do banner")
    private String bannerUrl;

    @Schema(description = "Horário de funcionamento")
    private String openingHours;

    @Schema(description = "Barbearia ativa")
    private Boolean active;

    // Endereço
    private String street;
    private Integer number;
    private String neighborhood;
    private String city;
    private String state;
    private String cep;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
