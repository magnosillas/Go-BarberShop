package br.edu.ufape.gobarber.controller.response;

import br.edu.ufape.gobarber.model.ProductStock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO de resposta para estoque de produtos.
 * Segue o padrão da arquitetura base com construtor que recebe Model.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStockResponse {

    private Integer idStock;
    private String batchNumber;
    private Integer quantity;
    private LocalDate expirationDate;
    private LocalDate acquisitionDate;
    private ProductResponse product;

    /**
     * Construtor que converte uma entidade ProductStock para o DTO de resposta.
     * 
     * @param productStock Entidade ProductStock a ser convertida
     */
    public ProductStockResponse(ProductStock productStock) {
        this.idStock = productStock.getIdStock();
        this.batchNumber = productStock.getBatchNumber();
        this.quantity = productStock.getQuantity();
        this.expirationDate = productStock.getExpirationDate();
        this.acquisitionDate = productStock.getAcquisitionDate();
        
        if (productStock.getProduct() != null) {
            this.product = new ProductResponse(productStock.getProduct());
        }
    }
}
