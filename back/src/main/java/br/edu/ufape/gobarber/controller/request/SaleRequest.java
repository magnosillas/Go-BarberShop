package br.edu.ufape.gobarber.controller.request;

import br.edu.ufape.gobarber.model.Sale;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * DTO de requisição para criação e atualização de promoções.
 * Segue o padrão da arquitetura base com método toModel().
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome descritivo do cupom", example = "Oferta na Progressiva")
    private String name;

    @Positive(message = "Preço deve ser positivo")
    @Schema(description = "Preço promotional", example = "59.99")
    private double totalPrice;

    @Schema(description = "Data de inicio da promoção", example = "2024-08-11")
    private LocalDate startDate;

    @Future(message = "Data de encerramento deve ser no futuro")
    @Schema(description = "Data de encerramento da promoção", example = "2024-09-11")
    private LocalDate endDate;

    @Schema(description = "Cupom de desconto de 7 caracteres", example = "PR12B02")
    private String coupon;

    /**
     * Converte o DTO de requisição para a entidade Sale.
     * 
     * @return Nova instância de Sale com os dados do request
     */
    public Sale toModel() {
        Sale sale = new Sale();
        sale.setName(this.name);
        sale.setTotalPrice(this.totalPrice);
        sale.setStartDate(this.startDate);
        sale.setEndDate(this.endDate);
        sale.setCoupon(this.coupon);
        return sale;
    }

    /**
     * Atualiza uma entidade Sale existente com os dados do request.
     * 
     * @param sale Entidade existente a ser atualizada
     */
    public void updateModel(Sale sale) {
        sale.setName(this.name);
        sale.setTotalPrice(this.totalPrice);
        sale.setStartDate(this.startDate);
        sale.setEndDate(this.endDate);
        sale.setCoupon(this.coupon);
    }
}
