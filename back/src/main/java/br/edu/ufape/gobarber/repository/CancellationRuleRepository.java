package br.edu.ufape.gobarber.repository;

import br.edu.ufape.gobarber.model.CancellationRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CancellationRuleRepository extends JpaRepository<CancellationRule, Integer> {
    Optional<CancellationRule> findFirstByActiveTrue();
}
