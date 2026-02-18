package br.edu.ufape.gobarber.dto.barberschedule;

import br.edu.ufape.gobarber.model.BarberSchedule;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO para transferência de dados de bloqueio/agenda do barbeiro.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberScheduleDTO {

    private Long idSchedule;
    private Long barberId;
    private String barberName;
    private BarberSchedule.ScheduleType scheduleType;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalTime startTime;
    private LocalTime endTime;
    private DayOfWeek dayOfWeek;
    private String reason;
    private Boolean isRecurring;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Alias para compatibilidade - retorna scheduleType
     */
    public BarberSchedule.ScheduleType getBlockType() {
        return scheduleType;
    }

    /**
     * Alias para compatibilidade - define scheduleType
     */
    public void setBlockType(BarberSchedule.ScheduleType type) {
        this.scheduleType = type;
    }

    /**
     * Verifica se é recorrente
     */
    public boolean isRecurring() {
        return Boolean.TRUE.equals(isRecurring);
    }

    /**
     * Verifica se está ativo
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }

    /**
     * Alias para isActive
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }

    /**
     * Alias para isRecurring
     */
    public void setRecurring(boolean recurring) {
        this.isRecurring = recurring;
    }

    /**
     * Converte uma entidade BarberSchedule para DTO.
     */
    public static BarberScheduleDTO fromEntity(BarberSchedule schedule) {
        if (schedule == null) {
            return null;
        }

        BarberScheduleDTOBuilder builder = BarberScheduleDTO.builder()
                .idSchedule(schedule.getIdSchedule())
                .scheduleType(schedule.getScheduleType())
                .startDateTime(schedule.getStartDateTime())
                .endDateTime(schedule.getEndDateTime())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .dayOfWeek(schedule.getDayOfWeek())
                .reason(schedule.getReason())
                .isRecurring(schedule.getIsRecurring())
                .isActive(schedule.getIsActive())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt());

        if (schedule.getBarber() != null) {
            builder.barberId(schedule.getBarber().getIdBarber().longValue())
                   .barberName(schedule.getBarber().getName());
        }

        return builder.build();
    }

    /**
     * Converte DTO para entidade BarberSchedule (sem barbeiro).
     */
    public BarberSchedule toEntity() {
        return BarberSchedule.builder()
                .idSchedule(this.idSchedule)
                .scheduleType(this.scheduleType)
                .startDateTime(this.startDateTime)
                .endDateTime(this.endDateTime)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .dayOfWeek(this.dayOfWeek)
                .reason(this.reason)
                .isRecurring(this.isRecurring)
                .isActive(this.isActive)
                .build();
    }
}
