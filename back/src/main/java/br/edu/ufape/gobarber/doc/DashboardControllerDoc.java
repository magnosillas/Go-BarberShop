package br.edu.ufape.gobarber.doc;

import br.edu.ufape.gobarber.dto.dashboard.DashboardDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Tag(name = "Dashboard", description = "API para métricas, relatórios e indicadores de desempenho")
public interface DashboardControllerDoc {

    @Operation(summary = "Dashboard principal", description = "Retorna todas as métricas do dashboard para o período especificado")
    @ApiResponse(responseCode = "200", description = "Dashboard gerado com sucesso",
            content = @Content(schema = @Schema(implementation = DashboardDTO.class)))
    ResponseEntity<DashboardDTO> getDashboard(
            @Parameter(description = "Data inicial") LocalDateTime startDate,
            @Parameter(description = "Data final") LocalDateTime endDate);

    @Operation(summary = "Dashboard do dia", description = "Retorna métricas do dia atual")
    ResponseEntity<DashboardDTO> getTodayDashboard();

    @Operation(summary = "Dashboard da semana", description = "Retorna métricas dos últimos 7 dias")
    ResponseEntity<DashboardDTO> getWeekDashboard();

    @Operation(summary = "Dashboard do mês", description = "Retorna métricas do mês atual")
    ResponseEntity<DashboardDTO> getMonthDashboard();

    @Operation(summary = "Dashboard do ano", description = "Retorna métricas do ano atual")
    ResponseEntity<DashboardDTO> getYearDashboard();

    // === Relatórios Específicos ===

    @Operation(summary = "Relatório financeiro", description = "Relatório detalhado de receitas, despesas e lucros")
    ResponseEntity<Map<String, Object>> getFinancialReport(LocalDateTime startDate, LocalDateTime endDate);

    @Operation(summary = "Relatório de clientes", description = "Relatório de novos clientes, retenção e fidelidade")
    ResponseEntity<Map<String, Object>> getClientReport(LocalDateTime startDate, LocalDateTime endDate);

    @Operation(summary = "Relatório de barbeiros", description = "Relatório de desempenho dos barbeiros")
    ResponseEntity<Map<String, Object>> getBarberReport(LocalDateTime startDate, LocalDateTime endDate);

    @Operation(summary = "Relatório de serviços", description = "Relatório de serviços mais realizados")
    ResponseEntity<Map<String, Object>> getServicesReport(LocalDateTime startDate, LocalDateTime endDate);

    // === Tempo Real ===

    @Operation(summary = "Agendamentos de hoje", description = "Métricas em tempo real de agendamentos do dia")
    ResponseEntity<Map<String, Object>> getAppointmentsToday();

    @Operation(summary = "Status dos barbeiros", description = "Status atual de todos os barbeiros")
    ResponseEntity<List<Map<String, Object>>> getBarbersStatus();

    @Operation(summary = "Receita hoje (tempo real)", description = "Receita atualizada do dia atual")
    ResponseEntity<Map<String, Object>> getRevenueTodayRealtime();

    // === Comparativos ===

    @Operation(summary = "Comparar períodos", description = "Compara métricas entre dois períodos")
    ResponseEntity<Map<String, Object>> comparePeriods(
            @Parameter(description = "Início do período 1") LocalDateTime period1Start,
            @Parameter(description = "Fim do período 1") LocalDateTime period1End,
            @Parameter(description = "Início do período 2") LocalDateTime period2Start,
            @Parameter(description = "Fim do período 2") LocalDateTime period2End);

    @Operation(summary = "Comparativo mês a mês", description = "Compara o mês atual com o anterior")
    ResponseEntity<Map<String, Object>> compareMonthOverMonth();

    @Operation(summary = "Comparativo ano a ano", description = "Compara o ano atual com o anterior")
    ResponseEntity<Map<String, Object>> compareYearOverYear();

    // === Tendências ===

    @Operation(summary = "Tendência de receita", description = "Evolução da receita nos últimos dias")
    ResponseEntity<List<Map<String, Object>>> getRevenueTrend(
            @Parameter(description = "Número de dias") int days);

    @Operation(summary = "Tendência de agendamentos", description = "Evolução de agendamentos nos últimos dias")
    ResponseEntity<List<Map<String, Object>>> getAppointmentsTrend(int days);

    @Operation(summary = "Tendência de clientes", description = "Evolução de novos clientes nos últimos dias")
    ResponseEntity<List<Map<String, Object>>> getClientsTrend(int days);

    // === KPIs ===

    @Operation(summary = "KPIs gerais", description = "Indicadores-chave de desempenho do negócio")
    ResponseEntity<Map<String, Object>> getKPIs();

    @Operation(summary = "KPIs do barbeiro", description = "Indicadores de desempenho de um barbeiro específico")
    ResponseEntity<Map<String, Object>> getBarberKPIs(@Parameter(description = "ID do barbeiro") Long barberId);
}
