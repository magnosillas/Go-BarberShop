package br.edu.ufape.gobarber.doc;

import br.edu.ufape.gobarber.dto.page.PageDTO;
import br.edu.ufape.gobarber.dto.review.ReviewCreateDTO;
import br.edu.ufape.gobarber.dto.review.ReviewDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Tag(name = "Avaliações", description = "API para gerenciamento de avaliações de clientes")
public interface ReviewControllerDoc {

    @Operation(summary = "Criar avaliação", description = "Cria uma nova avaliação para um atendimento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso",
                    content = @Content(schema = @Schema(implementation = ReviewDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado"),
            @ApiResponse(responseCode = "409", description = "Agendamento já avaliado")
    })
    ResponseEntity<ReviewDTO> create(ReviewCreateDTO dto) throws DataBaseException;

    @Operation(summary = "Buscar avaliação", description = "Retorna uma avaliação específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação encontrada"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
    })
    ResponseEntity<ReviewDTO> findById(@Parameter(description = "ID da avaliação") Long id) throws NotFoundException;

    @Operation(summary = "Listar avaliações", description = "Retorna lista paginada de avaliações")
    ResponseEntity<PageDTO<ReviewDTO>> findAll(Pageable pageable);

    @Operation(summary = "Excluir avaliação", description = "Remove uma avaliação do sistema")
    ResponseEntity<Void> delete(@Parameter(description = "ID da avaliação") Long id) throws NotFoundException;

    // === Moderação ===

    @Operation(summary = "Responder avaliação", description = "Adiciona uma resposta do estabelecimento à avaliação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resposta adicionada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
    })
    ResponseEntity<ReviewDTO> addReply(
            @Parameter(description = "ID da avaliação") Long id,
            @Parameter(description = "Resposta") Map<String, String> body) throws NotFoundException;

    @Operation(summary = "Ocultar avaliação", description = "Oculta uma avaliação da exibição pública")
    ResponseEntity<ReviewDTO> hide(@Parameter(description = "ID da avaliação") Long id) throws NotFoundException;

    @Operation(summary = "Exibir avaliação", description = "Torna uma avaliação visível novamente")
    ResponseEntity<ReviewDTO> show(@Parameter(description = "ID da avaliação") Long id) throws NotFoundException;

    // === Consultas por Barbeiro ===

    @Operation(summary = "Avaliações do barbeiro", description = "Lista todas as avaliações de um barbeiro")
    ResponseEntity<PageDTO<ReviewDTO>> findByBarber(
            @Parameter(description = "ID do barbeiro") Long barberId, Pageable pageable);

    @Operation(summary = "Avaliações visíveis do barbeiro", description = "Lista avaliações visíveis de um barbeiro")
    ResponseEntity<List<ReviewDTO>> findVisibleByBarber(@Parameter(description = "ID do barbeiro") Long barberId);

    @Operation(summary = "Média de avaliações", description = "Retorna a média de avaliações do barbeiro")
    ResponseEntity<Double> getAverageRatingByBarber(@Parameter(description = "ID do barbeiro") Long barberId);

    @Operation(summary = "Quantidade de avaliações", description = "Conta avaliações do barbeiro")
    ResponseEntity<Long> getReviewCountByBarber(@Parameter(description = "ID do barbeiro") Long barberId);

    @Operation(summary = "Distribuição de notas", description = "Retorna distribuição de notas do barbeiro")
    ResponseEntity<Map<Integer, Long>> getRatingDistributionByBarber(Long barberId);

    // === Consultas por Cliente ===

    @Operation(summary = "Avaliações do cliente", description = "Lista todas as avaliações feitas por um cliente")
    ResponseEntity<List<ReviewDTO>> findByClient(@Parameter(description = "ID do cliente") Long clientId);

    // === Consultas por Agendamento ===

    @Operation(summary = "Avaliação do agendamento", description = "Busca a avaliação de um agendamento específico")
    ResponseEntity<ReviewDTO> findByAppointment(
            @Parameter(description = "ID do agendamento") Long appointmentId) throws NotFoundException;

    @Operation(summary = "Verificar se tem avaliação", description = "Verifica se o agendamento já foi avaliado")
    ResponseEntity<Boolean> hasReview(@Parameter(description = "ID do agendamento") Long appointmentId);

    // === Filtros ===

    @Operation(summary = "Avaliações por nota mínima", description = "Lista avaliações com nota igual ou superior")
    ResponseEntity<List<ReviewDTO>> findByMinRating(
            @Parameter(description = "Nota mínima (1-5)") int minRating);

    @Operation(summary = "Avaliações recentes", description = "Lista as avaliações mais recentes")
    ResponseEntity<List<ReviewDTO>> findRecent(@Parameter(description = "Limite de resultados") int limit);

    @Operation(summary = "Avaliações em destaque", description = "Lista avaliações destacadas (5 estrelas)")
    ResponseEntity<List<ReviewDTO>> findFeatured(@Parameter(description = "Limite de resultados") int limit);

    // === Ranking ===

    @Operation(summary = "Ranking de barbeiros", description = "Lista barbeiros ordenados por avaliação")
    ResponseEntity<List<Map<String, Object>>> getBarberRanking(
            @Parameter(description = "Limite de resultados") int limit);

    // === Estatísticas ===

    @Operation(summary = "Média geral", description = "Retorna a média geral de todas as avaliações")
    ResponseEntity<Double> getOverallAverageRating();

    @Operation(summary = "Total de avaliações", description = "Conta todas as avaliações")
    ResponseEntity<Long> getTotalReviewCount();

    @Operation(summary = "Taxa de recomendação", description = "Percentual de clientes que recomendariam")
    ResponseEntity<Double> getRecommendationRate();

    @Operation(summary = "Distribuição geral", description = "Distribuição de notas de todas as avaliações")
    ResponseEntity<Map<Integer, Long>> getOverallRatingDistribution();

    @Operation(summary = "Estatísticas mensais", description = "Estatísticas de avaliações por mês")
    ResponseEntity<List<Map<String, Object>>> getMonthlyReviewStats(int months);

    @Operation(summary = "Pendentes de resposta", description = "Lista avaliações sem resposta")
    ResponseEntity<List<ReviewDTO>> getPendingReply();
}
