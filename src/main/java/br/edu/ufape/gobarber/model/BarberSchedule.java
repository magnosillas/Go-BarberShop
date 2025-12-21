package br.edu.ufape.gobarber.model;

import lombok.*;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entidade BarberSchedule - Bloqueio e configuração de horários dos barbeiros.
 * Permite férias, folgas, horários especiais.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "barber_schedule")
public class BarberSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_schedule")
    private Long idSchedule;

    @ManyToOne
    @JoinColumn(name = "barber_id", nullable = false)
    private Barber barber;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_type", nullable = false, length = 30)
    private ScheduleType scheduleType;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "reason")
    private String reason;

    @Column(name = "is_recurring", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isRecurring = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", length = 20)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Verifica se um horário específico está bloqueado.
     */
    public boolean isBlocked(LocalDateTime dateTime) {
        // Verifica se a data está dentro do período
        if (dateTime.isBefore(startDateTime)) return false;
        if (endDateTime != null && dateTime.isAfter(endDateTime)) return false;

        // Se for recorrente, verifica o dia da semana
        if (isRecurring && dayOfWeek != null) {
            if (dateTime.getDayOfWeek() != dayOfWeek) return false;
        }

        // Se tiver horários específicos, verifica
        if (startTime != null && endTime != null) {
            LocalTime time = dateTime.toLocalTime();
            return !time.isBefore(startTime) && !time.isAfter(endTime);
        }

        return true; // Período bloqueado
    }

    // Enums
    public enum ScheduleType {
        VACATION,       // Férias
        DAY_OFF,        // Folga
        LUNCH_BREAK,    // Almoço
        SPECIAL_HOURS,  // Horário especial
        TRAINING,       // Treinamento
        SICK_LEAVE,     // Atestado médico
        BLOCKED         // Bloqueio genérico
    }
}
