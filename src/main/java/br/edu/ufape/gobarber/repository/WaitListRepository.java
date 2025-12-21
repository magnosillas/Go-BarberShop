package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.WaitList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WaitListRepository extends JpaRepository<WaitList, Long> {

    Page<WaitList> findByClientIdClient(Long clientId, Pageable pageable);

    Page<WaitList> findByBarberIdBarber(Long barberId, Pageable pageable);

    List<WaitList> findByStatus(WaitList.WaitListStatus status);

    @Query("SELECT w FROM WaitList w WHERE w.status = 'WAITING' AND " +
           "w.barber.idBarber = :barberId ORDER BY w.priority DESC, w.createdAt ASC")
    List<WaitList> findWaitingByBarber(@Param("barberId") Long barberId);

    @Query("SELECT w FROM WaitList w WHERE w.status = 'WAITING' AND " +
           "w.desiredTime <= :date " +
           "ORDER BY w.priority DESC, w.createdAt ASC")
    List<WaitList> findWaitingUntil(@Param("date") LocalDateTime date);

    @Query("SELECT w FROM WaitList w WHERE w.status = 'WAITING' AND " +
           "w.expirationTime < :now")
    List<WaitList> findExpired(@Param("now") LocalDateTime now);

    @Query("SELECT COUNT(w) FROM WaitList w WHERE w.barber.idBarber = :barberId " +
           "AND w.status = 'WAITING'")
    Long countWaitingByBarber(@Param("barberId") Long barberId);

    @Query("SELECT COUNT(w) FROM WaitList w WHERE w.status = 'WAITING'")
    Long countTotalWaiting();

    @Query("SELECT w.barber.idBarber, w.barber.name, COUNT(w) FROM WaitList w " +
           "WHERE w.status = 'WAITING' GROUP BY w.barber.idBarber, w.barber.name")
    List<Object[]> countWaitingGroupByBarber();

    @Query("SELECT MAX(w.position) FROM WaitList w")
    Integer findMaxPosition();

    boolean existsByClientIdClientAndStatusAndBarberIdBarber(
            Long clientId, 
            WaitList.WaitListStatus status, 
            Long barberId);

    // Additional methods for WaitListService
    List<WaitList> findByClientIdClientOrderByCreatedAtDesc(Long clientId);

    List<WaitList> findByBarberIdBarberAndStatusOrderByPriorityAscCreatedAtAsc(
            Long barberId, WaitList.WaitListStatus status);

    List<WaitList> findByDesiredTimeAndStatusOrderByPriorityAscCreatedAtAsc(
            LocalDateTime desiredTime, WaitList.WaitListStatus status);

    List<WaitList> findByStatusOrderByPriorityAscCreatedAtAsc(WaitList.WaitListStatus status);

    Long countByStatus(WaitList.WaitListStatus status);

    @Query("SELECT COUNT(w) FROM WaitList w WHERE CAST(w.desiredTime AS date) = :desiredDate AND w.status IN :statuses")
    Long countByDesiredDateAndStatusIn(@Param("desiredDate") LocalDate desiredDate, @Param("statuses") List<WaitList.WaitListStatus> statuses);

    @Query("SELECT COUNT(w) FROM WaitList w WHERE CAST(w.desiredTime AS date) = :desiredDate AND w.status = :status " +
           "AND (w.priority < :priority OR (w.priority = :priority AND w.createdAt < :createdAt))")
    Long countByDesiredDateAndStatusAndPriorityLessThanOrCreatedAtBefore(
            @Param("desiredDate") LocalDate desiredDate, 
            @Param("status") WaitList.WaitListStatus status, 
            @Param("priority") WaitList.WaitListPriority priority, 
            @Param("createdAt") LocalDateTime createdAt);
}
