package br.edu.ufape.gobarber.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AvailabilityDTO {

    private Integer barberId;
    private String barberName;
    private String date;
    private List<TimeSlot> availableSlots;

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class TimeSlot {
        private String start;
        private String end;
    }
}
