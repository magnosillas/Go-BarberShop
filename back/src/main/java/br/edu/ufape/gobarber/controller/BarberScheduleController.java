package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.doc.BarberScheduleControllerDoc;
import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.model.BarberSchedule;
import br.edu.ufape.gobarber.service.BarberScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/barber-schedule")
@RequiredArgsConstructor
public class BarberScheduleController implements BarberScheduleControllerDoc {

    private final BarberScheduleService barberScheduleService;

    // === Criar Bloqueios ===

    @PostMapping("/block")
    public ResponseEntity<BarberSchedule> createBlock(
            @RequestParam Long barberId,
            @RequestParam BarberSchedule.ScheduleType type,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime,
            @RequestParam(required = false) String reason,
            @RequestParam(defaultValue = "false") boolean recurring,
            @RequestParam(required = false) DayOfWeek dayOfWeek) {
        
        BarberSchedule schedule = barberScheduleService.createScheduleBlock(
                barberId, type, startDateTime, endDateTime, reason, recurring, dayOfWeek);
        return new ResponseEntity<>(schedule, HttpStatus.CREATED);
    }

    @PostMapping("/vacation")
    public ResponseEntity<BarberSchedule> createVacation(
            @RequestParam Long barberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String reason) {
        
        BarberSchedule schedule = barberScheduleService.createVacation(barberId, startDate, endDate, reason);
        return new ResponseEntity<>(schedule, HttpStatus.CREATED);
    }

    @PostMapping("/day-off")
    public ResponseEntity<BarberSchedule> createDayOff(
            @RequestParam Long barberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam(required = false) String reason) {
        
        BarberSchedule schedule = barberScheduleService.createDayOff(barberId, date, reason);
        return new ResponseEntity<>(schedule, HttpStatus.CREATED);
    }

    @PostMapping("/lunch-break")
    public ResponseEntity<BarberSchedule> createRecurringLunchBreak(
            @RequestParam Long barberId,
            @RequestParam DayOfWeek dayOfWeek,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
        
        BarberSchedule schedule = barberScheduleService.createRecurringLunchBreak(
                barberId, dayOfWeek, startTime, endTime);
        return new ResponseEntity<>(schedule, HttpStatus.CREATED);
    }

    // === Consultas ===

    @GetMapping("/barber/{barberId}")
    public ResponseEntity<List<BarberSchedule>> getBarberScheduleBlocks(
            @PathVariable Long barberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(barberScheduleService.getBarberScheduleBlocks(barberId, startDate, endDate));
    }

    @GetMapping("/barber/{barberId}/vacations")
    public ResponseEntity<List<BarberSchedule>> getBarberVacations(@PathVariable Long barberId) {
        return ResponseEntity.ok(barberScheduleService.getBarberVacations(barberId));
    }

    @GetMapping("/barber/{barberId}/recurring")
    public ResponseEntity<List<BarberSchedule>> getRecurringSchedules(@PathVariable Long barberId) {
        return ResponseEntity.ok(barberScheduleService.getRecurringSchedules(barberId));
    }

    // === Disponibilidade ===

    @GetMapping("/availability/check")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam Long barberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ResponseEntity.ok(barberScheduleService.isBarberAvailable(barberId, startTime, endTime));
    }

    @GetMapping("/availability/slots")
    public ResponseEntity<List<LocalDateTime>> getAvailableSlots(
            @RequestParam Long barberId,
            @RequestParam String date,
            @RequestParam(defaultValue = "30") int slotDurationMinutes) {
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(date);
        } catch (Exception e) {
            dateTime = java.time.LocalDate.parse(date).atStartOfDay();
        }
        return ResponseEntity.ok(barberScheduleService.getAvailableSlots(barberId, dateTime, slotDurationMinutes));
    }

    @GetMapping("/availability/barbers")
    public ResponseEntity<List<Barber>> getAvailableBarbers(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ResponseEntity.ok(barberScheduleService.getAvailableBarbersForSlot(startTime, endTime));
    }

    // === Atualização e Exclusão ===

    @PutMapping("/{id}")
    public ResponseEntity<BarberSchedule> updateBlock(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(barberScheduleService.updateScheduleBlock(id, startDateTime, endDateTime, reason));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlock(@PathVariable Long id) {
        barberScheduleService.deleteScheduleBlock(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateBlock(@PathVariable Long id) {
        barberScheduleService.deactivateScheduleBlock(id);
        return ResponseEntity.ok().build();
    }

    // === Estatísticas ===

    @GetMapping("/barber/{barberId}/vacation-days")
    public ResponseEntity<Map<String, Object>> getVacationDays(
            @PathVariable Long barberId,
            @RequestParam(defaultValue = "2024") int year) {
        long days = barberScheduleService.countVacationDays(barberId, year);
        return ResponseEntity.ok(Map.of("barberId", barberId, "year", year, "vacationDays", days));
    }
}
