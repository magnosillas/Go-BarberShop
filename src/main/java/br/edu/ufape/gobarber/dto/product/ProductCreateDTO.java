package br.edu.ufape.gobarber.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductCreateDTO {
    private String name;
    private String brand;
    private String description;
    private double price;
    private String size;
}
