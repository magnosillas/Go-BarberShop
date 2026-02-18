package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.Appointment;
import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    Page<Appointment> findAllByOrderByStartTimeAsc(Pageable pageable);
    List<Appointment> findByBarberAndStartTimeBetween(Barber barber, LocalDateTime start, LocalDateTime end);
    Page<Appointment> findByBarber(Barber barber, Pageable pageable);
    Page<Appointment> findByBarberAndEndTimeBeforeOrderByStartTime(Pageable pageable, Barber barber, LocalDateTime dateTime);
    Page<Appointment> findByStartTimeAfterOrderByStartTime(Pageable pageable, LocalDateTime dateTime);
    Page<Appointment> findByBarberAndStartTimeAfterOrderByStartTime(Pageable pageable,  Barber barber, LocalDateTime dateTime);
    
    // Métodos para Dashboard
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.startTime BETWEEN :startDateTime AND :endDateTime")
    Long countByStartTimeBetween(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);
    
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.startTime < :dateTime")
    Long countByStartTimeBefore(@Param("dateTime") LocalDateTime dateTime);

    // Métodos para workflow de aprovação
    Page<Appointment> findByStatusOrderByStartTimeAsc(Appointment.AppointmentStatus status, Pageable pageable);

    Page<Appointment> findByStatusAndBarberOrderByStartTimeAsc(
            Appointment.AppointmentStatus status, Barber barber, Pageable pageable);

    Page<Appointment> findByClientOrderByStartTimeDesc(Client client, Pageable pageable);

    Page<Appointment> findByClientAndStatusOrderByStartTimeDesc(
            Client client, Appointment.AppointmentStatus status, Pageable pageable);

    // Buscar agendamentos confirmados de um barbeiro em um dia (para calcular disponibilidade)
    @Query("SELECT a FROM Appointment a WHERE a.barber = :barber " +
           "AND a.status IN ('CONFIRMED', 'PENDING_APPROVAL') " +
           "AND a.startTime >= :dayStart AND a.startTime < :dayEnd " +
           "ORDER BY a.startTime")
    List<Appointment> findActiveAppointmentsByBarberAndDate(
            @Param("barber") Barber barber,
            @Param("dayStart") LocalDateTime dayStart,
            @Param("dayEnd") LocalDateTime dayEnd);
}
