package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.model.BarberSchedule;
import br.edu.ufape.gobarber.repository.BarberScheduleRepository;
import br.edu.ufape.gobarber.repository.BarberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BarberScheduleService {

    private final BarberScheduleRepository barberScheduleRepository;
    private final BarberRepository barberRepository;

    // === CRUD ===

    @Transactional
    public BarberSchedule createScheduleBlock(Long barberId, BarberSchedule.ScheduleType type,
                                               LocalDateTime startDateTime, LocalDateTime endDateTime,
                                               String reason, boolean recurring, DayOfWeek dayOfWeek) {
        Barber barber = barberRepository.findById(barberId.intValue())
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));

        // Valida conflitos
        if (!recurring) {
            List<BarberSchedule> conflicts = barberScheduleRepository.findConflictingSchedules(
                    barberId.intValue(), startDateTime, endDateTime);
            if (!conflicts.isEmpty()) {
                throw new RuntimeException("Já existe um bloqueio neste horário");
            }
        }

        BarberSchedule schedule = BarberSchedule.builder()
                .barber(barber)
                .scheduleType(type)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .reason(reason)
                .isRecurring(recurring)
                .dayOfWeek(dayOfWeek)
                .startTime(recurring ? startDateTime.toLocalTime() : null)
                .endTime(recurring ? endDateTime.toLocalTime() : null)
                .isActive(true)
                .build();

        return barberScheduleRepository.save(schedule);
    }

    @Transactional
    public BarberSchedule createVacation(Long barberId, LocalDateTime start, LocalDateTime end, String reason) {
        return createScheduleBlock(barberId, BarberSchedule.ScheduleType.VACATION,
                start, end, reason, false, null);
    }

    @Transactional
    public BarberSchedule createDayOff(Long barberId, LocalDateTime date, String reason) {
        LocalDateTime start = date.withHour(0).withMinute(0);
        LocalDateTime end = date.withHour(23).withMinute(59);
        return createScheduleBlock(barberId, BarberSchedule.ScheduleType.DAY_OFF,
                start, end, reason, false, null);
    }

    @Transactional
    public BarberSchedule createRecurringLunchBreak(Long barberId, DayOfWeek dayOfWeek,
                                                     LocalTime startTime, LocalTime endTime) {
        LocalDateTime start = LocalDateTime.now().with(dayOfWeek).with(startTime);
        LocalDateTime end = LocalDateTime.now().with(dayOfWeek).with(endTime);
        
        return createScheduleBlock(barberId, BarberSchedule.ScheduleType.LUNCH_BREAK,
                start, end, "Horário de almoço", true, dayOfWeek);
    }

    @Transactional
    public BarberSchedule updateScheduleBlock(Long scheduleId, LocalDateTime startDateTime,
                                               LocalDateTime endDateTime, String reason) {
        BarberSchedule schedule = barberScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Bloqueio não encontrado"));

        schedule.setStartDateTime(startDateTime);
        schedule.setEndDateTime(endDateTime);
        schedule.setReason(reason);

        return barberScheduleRepository.save(schedule);
    }

    @Transactional
    public void deleteScheduleBlock(Long scheduleId) {
        barberScheduleRepository.deleteById(scheduleId);
    }

    @Transactional
    public void deactivateScheduleBlock(Long scheduleId) {
        BarberSchedule schedule = barberScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Bloqueio não encontrado"));
        schedule.setIsActive(false);
        barberScheduleRepository.save(schedule);
    }

    // === Consultas ===

    public List<BarberSchedule> getBarberScheduleBlocks(Long barberId, LocalDateTime start, LocalDateTime end) {
        return barberScheduleRepository.findByBarberAndDateRange(barberId.intValue(), start, end);
    }

    public List<BarberSchedule> getBarberVacations(Long barberId) {
        return barberScheduleRepository.findByBarberAndType(barberId.intValue(), BarberSchedule.ScheduleType.VACATION);
    }

    public List<BarberSchedule> getRecurringSchedules(Long barberId) {
        return barberScheduleRepository.findRecurringByBarber(barberId.intValue());
    }

    // === Verificações de Disponibilidade ===

    public boolean isBarberAvailable(Long barberId, LocalDateTime startTime, LocalDateTime endTime) {
        // Verifica bloqueios específicos
        List<BarberSchedule> conflicts = barberScheduleRepository.findConflictingSchedules(
                barberId.intValue(), startTime, endTime);
        if (!conflicts.isEmpty()) {
            return false;
        }

        // Verifica bloqueios recorrentes
        DayOfWeek dayOfWeek = startTime.getDayOfWeek();
        LocalTime start = startTime.toLocalTime();
        LocalTime end = endTime.toLocalTime();
        
        List<BarberSchedule> recurringConflicts = barberScheduleRepository.findRecurringConflicts(
                barberId.intValue(), dayOfWeek, start, end);
        
        return recurringConflicts.isEmpty();
    }

    public List<LocalDateTime> getAvailableSlots(Long barberId, LocalDateTime date, int slotDurationMinutes) {
        List<LocalDateTime> availableSlots = new ArrayList<>();
        
        // Horário de funcionamento padrão: 8h às 20h
        LocalTime openTime = LocalTime.of(8, 0);
        LocalTime closeTime = LocalTime.of(20, 0);
        
        LocalDateTime currentSlot = date.with(openTime);
        LocalDateTime endOfDay = date.with(closeTime);

        while (currentSlot.plusMinutes(slotDurationMinutes).isBefore(endOfDay) || 
               currentSlot.plusMinutes(slotDurationMinutes).equals(endOfDay)) {
            
            LocalDateTime slotEnd = currentSlot.plusMinutes(slotDurationMinutes);
            
            if (isBarberAvailable(barberId, currentSlot, slotEnd)) {
                availableSlots.add(currentSlot);
            }
            
            currentSlot = currentSlot.plusMinutes(30); // Slots a cada 30 minutos
        }

        return availableSlots;
    }

    public List<Barber> getAvailableBarbersForSlot(LocalDateTime startTime, LocalDateTime endTime) {
        List<Barber> allBarbers = barberRepository.findAll();
        List<Barber> availableBarbers = new ArrayList<>();

        for (Barber barber : allBarbers) {
            if (isBarberAvailable(barber.getIdBarber().longValue(), startTime, endTime)) {
                availableBarbers.add(barber);
            }
        }

        return availableBarbers;
    }

    // === Estatísticas ===

    public long countVacationDays(Long barberId, int year) {
        LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endOfYear = LocalDateTime.of(year, 12, 31, 23, 59);
        
        List<BarberSchedule> vacations = barberScheduleRepository.findByBarberAndDateRange(barberId.intValue(), startOfYear, endOfYear)
                .stream()
                .filter(s -> s.getScheduleType() == BarberSchedule.ScheduleType.VACATION)
                .toList();
        
        return vacations.stream()
                .mapToLong(v -> java.time.Duration.between(v.getStartDateTime(), v.getEndDateTime()).toDays() + 1)
                .sum();
    }
}
