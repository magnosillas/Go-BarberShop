package br.edu.ufape.gobarber.controller.request;

import br.edu.ufape.gobarber.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * DTO de requisição para criação e atualização de produtos.
 * Segue o padrão da arquitetura base com método toModel().
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Nome do produto é obrigatório")
    private String name;
    
    @NotBlank(message = "Marca do produto é obrigatória")
    private String brand;
    
    private String description;
    
    @Positive(message = "Preço deve ser positivo")
    private double price;
    
    private String size;

    /**
     * Converte o DTO de requisição para a entidade Product.
     * 
     * @return Nova instância de Product com os dados do request
     */
    public Product toModel() {
        Product product = new Product();
        product.setName(this.name);
        product.setBrand(this.brand);
        product.setDescription(this.description);
        product.setPrice(this.price);
        product.setSize(this.size);
        return product;
    }

    /**
     * Atualiza uma entidade Product existente com os dados do request.
     * 
     * @param product Entidade existente a ser atualizada
     */
    public void updateModel(Product product) {
        product.setName(this.name);
        product.setBrand(this.brand);
        product.setDescription(this.description);
        product.setPrice(this.price);
        product.setSize(this.size);
    }
}
