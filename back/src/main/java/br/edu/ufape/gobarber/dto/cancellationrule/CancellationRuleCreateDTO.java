package br.edu.ufape.gobarber.dto.cancellationrule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CancellationRuleCreateDTO {
    private Integer cancelDeadlineHours;
    private Double cancellationFeePercentage;
    private Double noShowFeePercentage;
    private Integer maxCancellationsPerMonth;
    private Boolean allowReschedule;
    private Integer rescheduleDeadlineHours;
    private Boolean penaltyAfterMaxCancellations;
    private Integer blockDaysAfterMaxCancellations;
}
