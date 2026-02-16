package br.edu.ufape.gobarber.dto.payment;

import br.edu.ufape.gobarber.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateDTO {

    private Integer appointmentId;

    private Long clientId;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private Double amount;

    @NotNull(message = "Método de pagamento é obrigatório")
    private Payment.PaymentMethod paymentMethod;

    private String couponCode;

    private Integer loyaltyPointsUsed;

    private Integer barberId;

    private Double commissionRate;

    private Integer installments;

    private String notes;

    // Dados específicos por método
    private String cardLastDigits;
    private String cardBrand;
}
