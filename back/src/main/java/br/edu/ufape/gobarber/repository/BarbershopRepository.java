package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.model.Barbershop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarbershopRepository extends JpaRepository<Barbershop, Integer> {

    Optional<Barbershop> findBySlug(String slug);

    List<Barbershop> findByActiveTrue();

    @Query("SELECT b FROM Barbershop b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Barbershop> findByNameContainingIgnoreCase(@Param("name") String name);

    boolean existsBySlug(String slug);

    boolean existsByCnpj(String cnpj);

    @Query("SELECT b FROM Barbershop b JOIN b.clients c WHERE c.idClient = :clientId")
    List<Barbershop> findByClientId(@Param("clientId") Long clientId);

    @Query("SELECT br FROM Barbershop b JOIN b.barbers br WHERE b.slug = :slug AND br.active = true")
    List<Barber> findBarbersBySlug(@Param("slug") String slug);
}
