package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.doc.ReviewControllerDoc;
import br.edu.ufape.gobarber.dto.review.ReviewCreateDTO;
import br.edu.ufape.gobarber.dto.review.ReviewDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@Tag(name = "Avaliações", description = "API para gerenciamento de avaliações de clientes")
public class ReviewController implements ReviewControllerDoc {

    private final ReviewService reviewService;

    // === CRUD ===

    @PostMapping
    @Operation(summary = "Criar avaliação")
    public ResponseEntity<ReviewDTO> create(@RequestBody @Valid ReviewCreateDTO dto) throws DataBaseException {
        return new ResponseEntity<>(reviewService.createReview(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar avaliação por ID")
    public ResponseEntity<ReviewDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping
    @Operation(summary = "Listar avaliações")
    public ResponseEntity<Page<ReviewDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(reviewService.getAllReviews(page, size));
    }

    // === Moderação ===

    @PostMapping("/{id}/reply")
    @Operation(summary = "Responder avaliação")
    public ResponseEntity<ReviewDTO> addReply(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String reply = body.get("reply");
        return ResponseEntity.ok(reviewService.addReply(id, reply));
    }

    @PostMapping("/{id}/hide")
    @Operation(summary = "Ocultar avaliação")
    public ResponseEntity<Void> hide(@PathVariable Long id) {
        reviewService.hideReview(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/show")
    @Operation(summary = "Exibir avaliação")
    public ResponseEntity<Void> show(@PathVariable Long id) {
        reviewService.showReview(id);
        return ResponseEntity.ok().build();
    }

    // === Consultas por Barbeiro ===

    @GetMapping("/barber/{barberId}")
    @Operation(summary = "Avaliações do barbeiro")
    public ResponseEntity<Page<ReviewDTO>> findByBarber(
            @PathVariable Integer barberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(reviewService.getReviewsByBarber(barberId, page, size));
    }

    @GetMapping("/barber/{barberId}/top")
    @Operation(summary = "Melhores avaliações do barbeiro")
    public ResponseEntity<Page<ReviewDTO>> getTopReviewsByBarber(
            @PathVariable Integer barberId,
            @RequestParam(defaultValue = "4") int minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reviewService.getTopReviewsByBarber(barberId, minRating, page, size));
    }

    @GetMapping("/barber/{barberId}/average")
    @Operation(summary = "Média de avaliações do barbeiro")
    public ResponseEntity<Double> getAverageRatingByBarber(@PathVariable Integer barberId) {
        return ResponseEntity.ok(reviewService.getAverageRatingByBarber(barberId));
    }

    @GetMapping("/barber/{barberId}/count")
    @Operation(summary = "Quantidade de avaliações do barbeiro")
    public ResponseEntity<Long> getReviewCountByBarber(@PathVariable Integer barberId) {
        return ResponseEntity.ok(reviewService.getReviewCountByBarber(barberId));
    }

    @GetMapping("/barber/{barberId}/distribution")
    @Operation(summary = "Distribuição de notas do barbeiro")
    public ResponseEntity<List<Object[]>> getRatingDistributionByBarber(@PathVariable Integer barberId) {
        return ResponseEntity.ok(reviewService.getRatingDistributionByBarber(barberId));
    }

    // === Consultas por Cliente ===

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Avaliações do cliente")
    public ResponseEntity<Page<ReviewDTO>> findByClient(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(reviewService.getReviewsByClient(clientId, page, size));
    }

    // === Ranking de Barbeiros ===

    @GetMapping("/ranking/barbers")
    @Operation(summary = "Ranking de barbeiros por avaliação")
    public ResponseEntity<List<Object[]>> getBarberRanking() {
        return ResponseEntity.ok(reviewService.getBarberRanking());
    }

    // === Estatísticas Gerais ===

    @GetMapping("/stats/average")
    @Operation(summary = "Média geral de avaliações")
    public ResponseEntity<Double> getOverallAverageRating() {
        return ResponseEntity.ok(reviewService.getOverallAverageRating());
    }

    @GetMapping("/stats/recommendation-rate")
    @Operation(summary = "Taxa de recomendação")
    public ResponseEntity<Double> getRecommendationRate() {
        return ResponseEntity.ok(reviewService.getRecommendationRate());
    }

    // === Pendentes de Resposta ===

    @GetMapping("/pending-reply")
    @Operation(summary = "Avaliações pendentes de resposta")
    public ResponseEntity<List<ReviewDTO>> getPendingReply() {
        return ResponseEntity.ok(reviewService.getPendingReplies());
    }
}
