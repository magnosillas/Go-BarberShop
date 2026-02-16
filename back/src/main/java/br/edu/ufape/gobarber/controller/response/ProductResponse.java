package br.edu.ufape.gobarber.controller.response;

import br.edu.ufape.gobarber.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para produtos.
 * Segue o padrão da arquitetura base com construtor que recebe Model.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Integer id;
    private String name;
    private String brand;
    private String description;
    private double price;
    private String size;

    /**
     * Construtor que converte uma entidade Product para o DTO de resposta.
     * 
     * @param product Entidade Product a ser convertida
     */
    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.brand = product.getBrand();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.size = product.getSize();
    }
}
