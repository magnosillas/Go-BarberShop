package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    Optional<Client> findByPhone(String phone);

    Optional<Client> findByCpf(String cpf);

    Page<Client> findByActiveTrue(Pageable pageable);

    Page<Client> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Client> findByLoyaltyTier(Client.LoyaltyTier tier);

    List<Client> findByReceivePromotionsTrue();

    @Query("SELECT c FROM Client c WHERE c.birthDate IS NOT NULL AND " +
           "MONTH(c.birthDate) = :month AND DAY(c.birthDate) = :day")
    List<Client> findBirthdayClients(@Param("month") int month, @Param("day") int day);

    @Query("SELECT c FROM Client c WHERE c.lastVisit < :date AND c.active = true")
    List<Client> findInactiveClients(@Param("date") LocalDateTime date);

    @Query("SELECT c FROM Client c WHERE c.preferredBarber.idBarber = :barberId")
    List<Client> findByPreferredBarberId(@Param("barberId") Integer barberId);

    @Query("SELECT c FROM Client c ORDER BY c.totalSpent DESC")
    Page<Client> findTopSpenders(Pageable pageable);

    @Query("SELECT c FROM Client c ORDER BY c.totalVisits DESC")
    Page<Client> findMostFrequent(Pageable pageable);

    @Query("SELECT c FROM Client c ORDER BY c.loyaltyPoints DESC")
    Page<Client> findTopLoyaltyPoints(Pageable pageable);

    @Query("SELECT COUNT(c) FROM Client c WHERE c.createdAt >= :startDate")
    Long countNewClients(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT c.loyaltyTier, COUNT(c) FROM Client c GROUP BY c.loyaltyTier")
    List<Object[]> countByLoyaltyTier();

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsByPhone(String phone);
}
