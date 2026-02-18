package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findByUserId(Long userId, Pageable pageable);

    Page<AuditLog> findByUserEmail(String userEmail, Pageable pageable);

    Page<AuditLog> findByEntityType(String entityType, Pageable pageable);

    Page<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId, Pageable pageable);

    Page<AuditLog> findByAction(AuditLog.AuditAction action, Pageable pageable);

    @Query("SELECT a FROM AuditLog a WHERE a.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY a.createdAt DESC")
    Page<AuditLog> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query("SELECT a FROM AuditLog a WHERE a.success = false ORDER BY a.createdAt DESC")
    Page<AuditLog> findFailedActions(Pageable pageable);

    @Query("SELECT a.action, COUNT(a) FROM AuditLog a " +
           "WHERE a.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY a.action ORDER BY COUNT(a) DESC")
    List<Object[]> countByAction(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a.entityType, COUNT(a) FROM AuditLog a " +
           "WHERE a.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY a.entityType ORDER BY COUNT(a) DESC")
    List<Object[]> countByEntityType(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a.userId, a.userEmail, COUNT(a) FROM AuditLog a " +
           "WHERE a.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY a.userId, a.userEmail ORDER BY COUNT(a) DESC")
    List<Object[]> countByUser(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT AVG(a.executionTimeMs) FROM AuditLog a " +
           "WHERE a.requestUri = :uri AND a.createdAt BETWEEN :startDate AND :endDate")
    Double averageExecutionTime(
            @Param("uri") String uri,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Additional methods for AuditService
    List<AuditLog> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<AuditLog> findByActionOrderByCreatedAtDesc(AuditLog.AuditAction action);

    List<AuditLog> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(String entityType, Long entityId);

    List<AuditLog> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);

    List<AuditLog> findByIpAddressOrderByCreatedAtDesc(String ipAddress);

    Page<AuditLog> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<AuditLog> findByActionAndCreatedAtAfterOrderByCreatedAtDesc(
            AuditLog.AuditAction action, LocalDateTime after);

    Long countByActionAndIpAddressAndCreatedAtAfter(
            AuditLog.AuditAction action, String ipAddress, LocalDateTime after);

    @Modifying
    @Query("DELETE FROM AuditLog a WHERE a.createdAt < :before")
    void deleteByCreatedAtBefore(@Param("before") LocalDateTime before);

    @Query("SELECT a FROM AuditLog a WHERE a.ipAddress IN " +
           "(SELECT a2.ipAddress FROM AuditLog a2 " +
           "WHERE a2.action = 'LOGIN_FAILED' AND a2.createdAt > :after " +
           "GROUP BY a2.ipAddress HAVING COUNT(a2) >= :threshold)")
    List<AuditLog> findSuspiciousActivityByIp(@Param("threshold") Integer threshold, 
                                              @Param("after") LocalDateTime after);

    List<AuditLog> findByActionInAndCreatedAtAfterOrderByCreatedAtDesc(
            List<AuditLog.AuditAction> actions, LocalDateTime after);
}
