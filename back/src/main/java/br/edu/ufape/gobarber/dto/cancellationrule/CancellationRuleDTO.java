package br.edu.ufape.gobarber.dto.cancellationrule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CancellationRuleDTO {
    private Integer id;

    @Schema(description = "Horas mínimas de antecedência para cancelamento", example = "24")
    private Integer cancelDeadlineHours;

    @Schema(description = "Percentual de multa por cancelamento dentro do prazo", example = "0")
    private Double cancellationFeePercentage;

    @Schema(description = "Percentual de multa por não comparecimento", example = "100")
    private Double noShowFeePercentage;

    @Schema(description = "Máximo de cancelamentos por mês", example = "3")
    private Integer maxCancellationsPerMonth;

    @Schema(description = "Permite reagendamento", example = "true")
    private Boolean allowReschedule;

    @Schema(description = "Horas mínimas de antecedência para reagendamento", example = "12")
    private Integer rescheduleDeadlineHours;

    @Schema(description = "Aplicar penalidade após exceder máximo de cancelamentos", example = "true")
    private Boolean penaltyAfterMaxCancellations;

    @Schema(description = "Dias de bloqueio após exceder máximo de cancelamentos", example = "7")
    private Integer blockDaysAfterMaxCancellations;

    @Schema(description = "Regra ativa", example = "true")
    private Boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
