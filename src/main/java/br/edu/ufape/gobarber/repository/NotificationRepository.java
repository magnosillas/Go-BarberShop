package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByClientIdClient(Long clientId, Pageable pageable);

    Page<Notification> findByBarberIdBarber(Integer barberId, Pageable pageable);

    List<Notification> findByStatus(Notification.NotificationStatus status);

    List<Notification> findByStatusAndScheduledForBefore(
            Notification.NotificationStatus status, 
            LocalDateTime dateTime);

    @Query("SELECT n FROM Notification n WHERE n.status = 'PENDING' AND " +
           "(n.scheduledFor IS NULL OR n.scheduledFor <= :now)")
    List<Notification> findPendingToSend(@Param("now") LocalDateTime now);

    @Query("SELECT n FROM Notification n WHERE n.client.idClient = :clientId " +
           "AND n.status != 'READ' ORDER BY n.createdAt DESC")
    List<Notification> findUnreadByClient(@Param("clientId") Long clientId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.client.idClient = :clientId " +
           "AND n.status != 'READ'")
    Long countUnreadByClient(@Param("clientId") Long clientId);

    @Query("SELECT n FROM Notification n WHERE n.type = :type AND " +
           "n.createdAt BETWEEN :startDate AND :endDate")
    List<Notification> findByTypeAndDateRange(
            @Param("type") Notification.NotificationType type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT n.type, COUNT(n) FROM Notification n " +
           "WHERE n.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY n.type")
    List<Object[]> countByType(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT n.status, COUNT(n) FROM Notification n " +
           "WHERE n.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY n.status")
    List<Object[]> countByStatus(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT n FROM Notification n WHERE n.status = 'FAILED' AND n.retryCount < :maxRetries")
    List<Notification> findFailedForRetry(@Param("maxRetries") Integer maxRetries);

    // Additional methods for NotificationService
    List<Notification> findByClientIdClientOrderByCreatedAtDesc(Long clientId);

    List<Notification> findByClientIdClientAndStatusNot(Long clientId, Notification.NotificationStatus status);

    Long countByClientIdClientAndStatusNot(Long clientId, Notification.NotificationStatus status);

    List<Notification> findByType(Notification.NotificationType type);

    List<Notification> findByChannel(Notification.NotificationChannel channel);

    // Methods for Controller
    Page<Notification> findByClientIdClientOrderByCreatedAtDesc(Long clientId, Pageable pageable);

    Long countByStatus(Notification.NotificationStatus status);

    List<Notification> findByClientIdClientAndCreatedAtBefore(Long clientId, LocalDateTime cutoff);
}
