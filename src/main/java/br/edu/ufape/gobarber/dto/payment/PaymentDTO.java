package br.edu.ufape.gobarber.dto.payment;

import br.edu.ufape.gobarber.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@lombok.Getter
@lombok.Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    private Long idPayment;
    private Integer appointmentId;
    private Long clientId;
    private String clientName;
    private Double amount;
    private Double discountAmount;
    private Double finalAmount;
    private Payment.PaymentMethod paymentMethod;
    private Payment.PaymentStatus status;
    private String transactionId;
    private String couponCode;
    private Double couponDiscount;
    private Double loyaltyDiscount;
    private Integer loyaltyPointsUsed;
    private Integer loyaltyPointsEarned;
    private Integer barberId;
    private String barberName;
    private Double commissionRate;
    private Double commissionAmount;
    private Integer installments;
    private String notes;
    private LocalDateTime paymentDate;
    private LocalDateTime createdAt;

    // Dados PIX
    private String pixCode;
    private String pixQrCode;

    // Dados cartão
    private String cardLastDigits;
    private String cardBrand;

    public static PaymentDTO fromEntity(Payment payment) {
        PaymentDTO.PaymentDTOBuilder builder = PaymentDTO.builder()
                .idPayment(payment.getIdPayment())
                .amount(payment.getAmount())
                .discountAmount(payment.getDiscountAmount())
                .finalAmount(payment.getFinalAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .couponCode(payment.getCouponCode())
                .couponDiscount(payment.getCouponDiscount())
                .loyaltyDiscount(payment.getLoyaltyDiscount())
                .loyaltyPointsUsed(payment.getLoyaltyPointsUsed())
                .loyaltyPointsEarned(payment.getLoyaltyPointsEarned())
                .commissionRate(payment.getCommissionRate())
                .commissionAmount(payment.getCommissionAmount())
                .installments(payment.getInstallments())
                .notes(payment.getNotes())
                .paymentDate(payment.getPaymentDate())
                .createdAt(payment.getCreatedAt())
                .pixCode(payment.getPixCode())
                .pixQrCode(payment.getPixQrCode())
                .cardLastDigits(payment.getCardLastDigits())
                .cardBrand(payment.getCardBrand());

        if (payment.getAppointment() != null) {
            builder.appointmentId(payment.getAppointment().getId());
        }

        if (payment.getClient() != null) {
            builder.clientId(payment.getClient().getIdClient());
            builder.clientName(payment.getClient().getName());
        }

        if (payment.getBarber() != null) {
            builder.barberId(payment.getBarber().getIdBarber());
            builder.barberName(payment.getBarber().getName());
        }

        return builder.build();
    }
}
