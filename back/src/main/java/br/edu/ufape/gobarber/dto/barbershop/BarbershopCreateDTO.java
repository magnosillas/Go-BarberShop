package br.edu.ufape.gobarber.dto.barbershop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BarbershopCreateDTO {

    @NotBlank
    @Schema(description = "Nome da barbearia", example = "Barbearia Central")
    private String name;

    @Schema(description = "Slug único para URL", example = "barbearia-central")
    private String slug;

    @Schema(description = "Descrição da barbearia", example = "A melhor barbearia da cidade")
    private String description;

    @Schema(description = "CNPJ da barbearia", example = "12.345.678/0001-90")
    private String cnpj;

    @Schema(description = "Telefone de contato", example = "(81) 99999-0000")
    private String phone;

    @Schema(description = "Email de contato", example = "contato@barbearia.com")
    private String email;

    @Schema(description = "URL do logotipo")
    private String logoUrl;

    @Schema(description = "URL do banner")
    private String bannerUrl;

    @Schema(description = "Horário de funcionamento", example = "Seg-Sex 9h-20h, Sáb 9h-18h")
    private String openingHours;

    // Endereço inline
    @Schema(description = "Rua")
    private String street;

    @Schema(description = "Número")
    private Integer number;

    @Schema(description = "Bairro")
    private String neighborhood;

    @Schema(description = "Cidade")
    private String city;

    @Schema(description = "Estado")
    private String state;

    @Schema(description = "CEP")
    private String cep;
}
