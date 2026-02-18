package br.edu.ufape.gobarber.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cancellation_rule")
public class CancellationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cancellation_rule")
    private Integer id;

    @Column(name = "cancel_deadline_hours", nullable = false)
    private Integer cancelDeadlineHours;

    @Column(name = "cancellation_fee_percentage", nullable = false)
    private Double cancellationFeePercentage;

    @Column(name = "no_show_fee_percentage", nullable = false)
    private Double noShowFeePercentage;

    @Column(name = "max_cancellations_per_month", nullable = false)
    private Integer maxCancellationsPerMonth;

    @Column(name = "allow_reschedule", nullable = false)
    private Boolean allowReschedule;

    @Column(name = "reschedule_deadline_hours", nullable = false)
    private Integer rescheduleDeadlineHours;

    @Column(name = "penalty_after_max_cancellations", nullable = false)
    private Boolean penaltyAfterMaxCancellations;

    @Column(name = "block_days_after_max_cancellations", nullable = false)
    private Integer blockDaysAfterMaxCancellations;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
