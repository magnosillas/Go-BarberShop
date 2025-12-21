package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.BarberSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BarberScheduleRepository extends JpaRepository<BarberSchedule, Long> {

    List<BarberSchedule> findByBarberIdBarber(Long barberId);

    List<BarberSchedule> findByBarberIdBarberAndIsActiveTrue(Long barberId);

    @Query("SELECT bs FROM BarberSchedule bs WHERE bs.barber.idBarber = :barberId " +
           "AND bs.isActive = true AND " +
           "((bs.startDateTime <= :dateTime AND (bs.endDateTime IS NULL OR bs.endDateTime >= :dateTime)) " +
           "OR (bs.isRecurring = true AND bs.dayOfWeek = :dayOfWeek))")
    List<BarberSchedule> findActiveSchedulesForDate(
            @Param("barberId") Long barberId,
            @Param("dateTime") LocalDateTime dateTime,
            @Param("dayOfWeek") DayOfWeek dayOfWeek);

    @Query("SELECT bs FROM BarberSchedule bs WHERE bs.barber.idBarber = :barberId " +
           "AND bs.scheduleType = :type AND bs.isActive = true")
    List<BarberSchedule> findByBarberAndType(
            @Param("barberId") Long barberId,
            @Param("type") BarberSchedule.ScheduleType type);

    @Query("SELECT bs FROM BarberSchedule bs WHERE bs.barber.idBarber = :barberId " +
           "AND bs.startDateTime >= :startDateTime AND bs.startDateTime <= :endDateTime AND bs.isActive = true")
    List<BarberSchedule> findByBarberAndDateRange(
            @Param("barberId") Long barberId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);

    @Query("SELECT bs FROM BarberSchedule bs WHERE bs.scheduleType = 'VACATION' " +
           "AND bs.startDateTime <= :endDateTime AND (bs.endDateTime IS NULL OR bs.endDateTime >= :startDateTime) " +
           "AND bs.isActive = true")
    List<BarberSchedule> findVacationsInPeriod(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);

    void deleteByBarberIdBarberAndScheduleType(Long barberId, BarberSchedule.ScheduleType type);

    // Additional methods for BarberScheduleService
    List<BarberSchedule> findByBarberIdBarberAndStartDateTimeBetweenAndIsActiveTrue(
            Long barberId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<BarberSchedule> findByScheduleTypeAndStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqualAndIsActiveTrue(
            BarberSchedule.ScheduleType scheduleType, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<BarberSchedule> findByScheduleTypeAndStartDateTimeAndIsActiveTrue(
            BarberSchedule.ScheduleType scheduleType, LocalDateTime dateTime);

    List<BarberSchedule> findByBarberIdBarberAndStartDateTimeAndIsActiveTrue(
            Long barberId, LocalDateTime dateTime);

    // Methods for conflict detection
    @Query("SELECT bs FROM BarberSchedule bs WHERE bs.barber.idBarber = :barberId " +
           "AND bs.isActive = true AND bs.isRecurring = false " +
           "AND ((bs.startDateTime <= :endDateTime AND bs.endDateTime >= :startDateTime))")
    List<BarberSchedule> findConflictingSchedules(
            @Param("barberId") Long barberId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);

    // Methods for recurring schedules
    @Query("SELECT bs FROM BarberSchedule bs WHERE bs.barber.idBarber = :barberId " +
           "AND bs.isActive = true AND bs.isRecurring = true")
    List<BarberSchedule> findRecurringByBarber(@Param("barberId") Long barberId);

    @Query("SELECT bs FROM BarberSchedule bs WHERE bs.barber.idBarber = :barberId " +
           "AND bs.isActive = true AND bs.isRecurring = true " +
           "AND bs.dayOfWeek = :dayOfWeek " +
           "AND ((bs.startTime <= :endTime AND bs.endTime >= :startTime))")
    List<BarberSchedule> findRecurringConflicts(
            @Param("barberId") Long barberId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}
