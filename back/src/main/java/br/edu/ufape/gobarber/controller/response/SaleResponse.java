package br.edu.ufape.gobarber.controller.response;

import br.edu.ufape.gobarber.model.Sale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO de resposta para promoções.
 * Segue o padrão da arquitetura base com construtor que recebe Model.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleResponse {

    private Integer idSale;
    private String name;
    private double totalPrice;
    private String coupon;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Construtor que converte uma entidade Sale para o DTO de resposta.
     * 
     * @param sale Entidade Sale a ser convertida
     */
    public SaleResponse(Sale sale) {
        this.idSale = sale.getIdSale();
        this.name = sale.getName();
        this.totalPrice = sale.getTotalPrice();
        this.coupon = sale.getCoupon();
        this.startDate = sale.getStartDate();
        this.endDate = sale.getEndDate();
    }
}
