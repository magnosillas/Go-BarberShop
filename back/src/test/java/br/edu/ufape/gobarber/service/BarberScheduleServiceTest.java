package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.model.BarberSchedule;
import br.edu.ufape.gobarber.repository.BarberRepository;
import br.edu.ufape.gobarber.repository.BarberScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BarberScheduleServiceTest {

    @Mock
    private BarberScheduleRepository barberScheduleRepository;

    @Mock
    private BarberRepository barberRepository;

    @InjectMocks
    private BarberScheduleService barberScheduleService;

    private Barber barber;
    private BarberSchedule schedule;

    @BeforeEach
    void setUp() {
        barber = new Barber();
        barber.setIdBarber(1);
        barber.setName("Carlos Barbeiro");

        schedule = BarberSchedule.builder()
            .idSchedule(1L)
            .barber(barber)
            .scheduleType(BarberSchedule.ScheduleType.VACATION)
            .startDateTime(LocalDateTime.now())
            .endDateTime(LocalDateTime.now().plusDays(7))
            .reason("Férias de fim de ano")
            .isRecurring(false)
            .isActive(true)
            .build();
    }

    @Test
    void testCreateVacation_Success() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(7);

        when(barberRepository.findById(1)).thenReturn(Optional.of(barber));
        when(barberScheduleRepository.findConflictingSchedules(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(barberScheduleRepository.save(any(BarberSchedule.class))).thenReturn(schedule);

        BarberSchedule result = barberScheduleService.createVacation(1L, start, end, "Férias");

        assertNotNull(result);
        assertEquals(BarberSchedule.ScheduleType.VACATION, result.getScheduleType());
        verify(barberScheduleRepository, times(1)).save(any(BarberSchedule.class));
    }

    @Test
    void testCreateVacation_BarberNotFound() {
        when(barberRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> barberScheduleService.createVacation(1L, LocalDateTime.now(),
                LocalDateTime.now().plusDays(7), "Férias"));
    }

    @Test
    void testCreateVacation_ConflictingSchedule() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(7);

        when(barberRepository.findById(1)).thenReturn(Optional.of(barber));
        when(barberScheduleRepository.findConflictingSchedules(any(), any(), any()))
                .thenReturn(Arrays.asList(schedule));

        assertThrows(RuntimeException.class, () -> barberScheduleService.createVacation(1L, start, end, "Férias"));
    }

    @Test
    void testCreateDayOff_Success() {
        when(barberRepository.findById(1)).thenReturn(Optional.of(barber));
        when(barberScheduleRepository.findConflictingSchedules(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(barberScheduleRepository.save(any(BarberSchedule.class))).thenReturn(schedule);

        BarberSchedule result = barberScheduleService.createDayOff(1L, LocalDateTime.now().plusDays(1), "Folga");

        assertNotNull(result);
        verify(barberScheduleRepository, times(1)).save(any(BarberSchedule.class));
    }

    @Test
    void testCreateRecurringLunchBreak_Success() {
        when(barberRepository.findById(1)).thenReturn(Optional.of(barber));
        when(barberScheduleRepository.save(any(BarberSchedule.class))).thenReturn(schedule);

        BarberSchedule result = barberScheduleService.createRecurringLunchBreak(
                1L, DayOfWeek.MONDAY, LocalTime.of(12, 0), LocalTime.of(13, 0));

        assertNotNull(result);
        verify(barberScheduleRepository, times(1)).save(any(BarberSchedule.class));
    }

    @Test
    void testUpdateScheduleBlock_Success() {
        LocalDateTime newStart = LocalDateTime.now().plusDays(2);
        LocalDateTime newEnd = LocalDateTime.now().plusDays(10);

        when(barberScheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(barberScheduleRepository.save(any(BarberSchedule.class))).thenReturn(schedule);

        BarberSchedule result = barberScheduleService.updateScheduleBlock(1L, newStart, newEnd, "Férias estendidas");

        assertNotNull(result);
        verify(barberScheduleRepository, times(1)).findById(1L);
        verify(barberScheduleRepository, times(1)).save(any(BarberSchedule.class));
    }

    @Test
    void testUpdateScheduleBlock_NotFound() {
        when(barberScheduleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> barberScheduleService.updateScheduleBlock(1L, LocalDateTime.now(),
                LocalDateTime.now().plusDays(1), "Reason"));
    }

    @Test
    void testDeleteScheduleBlock_Success() {
        doNothing().when(barberScheduleRepository).deleteById(1L);

        barberScheduleService.deleteScheduleBlock(1L);

        verify(barberScheduleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeactivateScheduleBlock_Success() {
        when(barberScheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(barberScheduleRepository.save(any(BarberSchedule.class))).thenReturn(schedule);

        barberScheduleService.deactivateScheduleBlock(1L);

        verify(barberScheduleRepository, times(1)).findById(1L);
        verify(barberScheduleRepository, times(1)).save(any(BarberSchedule.class));
    }

    @Test
    void testDeactivateScheduleBlock_NotFound() {
        when(barberScheduleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> barberScheduleService.deactivateScheduleBlock(1L));
    }

    @Test
    void testGetBarberScheduleBlocks_Success() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(30);

        when(barberScheduleRepository.findByBarberAndDateRange(eq(1), any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(schedule));

        List<BarberSchedule> result = barberScheduleService.getBarberScheduleBlocks(1L, start, end);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetBarberVacations_Success() {
        when(barberScheduleRepository.findByBarberAndType(1, BarberSchedule.ScheduleType.VACATION))
                .thenReturn(Arrays.asList(schedule));

        List<BarberSchedule> result = barberScheduleService.getBarberVacations(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetRecurringSchedules_Success() {
        when(barberScheduleRepository.findRecurringByBarber(1)).thenReturn(Arrays.asList(schedule));

        List<BarberSchedule> result = barberScheduleService.getRecurringSchedules(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    // Testes da entidade BarberSchedule
    @Test
    void testBarberScheduleEntity_DefaultValues() {
        BarberSchedule newSchedule = new BarberSchedule();

        assertFalse(newSchedule.getIsRecurring());
        assertTrue(newSchedule.getIsActive());
    }

    @Test
    void testBarberScheduleEntity_SetterGetter() {
        BarberSchedule newSchedule = new BarberSchedule();
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(7);
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(17, 0);

        newSchedule.setBarber(barber);
        newSchedule.setScheduleType(BarberSchedule.ScheduleType.DAY_OFF);
        newSchedule.setStartDateTime(startDateTime);
        newSchedule.setEndDateTime(endDateTime);
        newSchedule.setStartTime(startTime);
        newSchedule.setEndTime(endTime);
        newSchedule.setDayOfWeek(DayOfWeek.FRIDAY);
        newSchedule.setReason("Folga semanal");
        newSchedule.setIsRecurring(true);
        newSchedule.setIsActive(true);

        assertEquals(barber, newSchedule.getBarber());
        assertEquals(BarberSchedule.ScheduleType.DAY_OFF, newSchedule.getScheduleType());
        assertEquals(startDateTime, newSchedule.getStartDateTime());
        assertEquals(endDateTime, newSchedule.getEndDateTime());
        assertEquals(startTime, newSchedule.getStartTime());
        assertEquals(endTime, newSchedule.getEndTime());
        assertEquals(DayOfWeek.FRIDAY, newSchedule.getDayOfWeek());
        assertEquals("Folga semanal", newSchedule.getReason());
        assertTrue(newSchedule.getIsRecurring());
        assertTrue(newSchedule.getIsActive());
    }

    @Test
    void testScheduleType_AllValues() {
        BarberSchedule.ScheduleType[] types = BarberSchedule.ScheduleType.values();

        assertTrue(types.length >= 3);
        assertNotNull(BarberSchedule.ScheduleType.VACATION);
        assertNotNull(BarberSchedule.ScheduleType.DAY_OFF);
        assertNotNull(BarberSchedule.ScheduleType.SPECIAL_HOURS);
    }
}
