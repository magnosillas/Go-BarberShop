package br.edu.ufape.gobarber.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {

    private Integer id;

    @Schema(description = "Nome do produto", example = "Pomada capilar")
    private String name;

    @Schema(description = "Marca do produto", example = "For men")
    private String brand;

    @Schema(description = "Descrição do produto", example = "Pomada desenvolvida por cientistas para alisar o cabelo")
    private String description;

    @Schema(description = "Preço do produto", example = "29")
    private double price;

    @Schema(description = "Tamanho do produto", example = "50g")
    private String size;
}
