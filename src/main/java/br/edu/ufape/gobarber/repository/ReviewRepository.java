package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByBarberIdBarber(Integer barberId, Pageable pageable);

    Page<Review> findByClientIdClient(Long clientId, Pageable pageable);

    Optional<Review> findByAppointmentId(Integer appointmentId);

    Page<Review> findByIsVisibleTrue(Pageable pageable);

    Page<Review> findByBarberIdBarberAndIsVisibleTrue(Integer barberId, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.barber.idBarber = :barberId AND r.isVisible = true")
    Double averageRatingByBarber(@Param("barberId") Integer barberId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.isVisible = true")
    Double overallAverageRating();

    @Query("SELECT r.rating, COUNT(r) FROM Review r WHERE r.barber.idBarber = :barberId " +
           "AND r.isVisible = true GROUP BY r.rating ORDER BY r.rating")
    List<Object[]> ratingDistributionByBarber(@Param("barberId") Integer barberId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.barber.idBarber = :barberId AND r.isVisible = true")
    Long countByBarber(@Param("barberId") Integer barberId);

    @Query("SELECT r.barber.idBarber, r.barber.name, AVG(r.rating), COUNT(r) " +
           "FROM Review r WHERE r.isVisible = true " +
           "GROUP BY r.barber.idBarber, r.barber.name " +
           "ORDER BY AVG(r.rating) DESC")
    List<Object[]> barberRankingByRating();

    @Query("SELECT COUNT(r) FROM Review r WHERE r.wouldRecommend = true AND r.isVisible = true")
    Long countWouldRecommend();

    @Query("SELECT COUNT(r) FROM Review r WHERE r.isVisible = true")
    Long countTotal();

    List<Review> findByReplyIsNullAndIsVisibleTrue();

    @Query("SELECT r FROM Review r WHERE r.barber.idBarber = :barberId AND r.rating >= :minRating " +
           "AND r.isVisible = true ORDER BY r.createdAt DESC")
    Page<Review> findTopReviewsByBarber(
            @Param("barberId") Integer barberId,
            @Param("minRating") Integer minRating,
            Pageable pageable);
}
