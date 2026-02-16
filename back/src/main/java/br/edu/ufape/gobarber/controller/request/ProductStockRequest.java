package br.edu.ufape.gobarber.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * DTO de requisição para criação e atualização de estoque de produtos.
 * Segue o padrão da arquitetura base.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStockRequest {

    private String batchNumber;
    
    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    private Integer quantity;
    
    private LocalDate expirationDate;
    
    private LocalDate acquisitionDate;
    
    @NotNull(message = "ID do produto é obrigatório")
    private Integer idProduct;
}
