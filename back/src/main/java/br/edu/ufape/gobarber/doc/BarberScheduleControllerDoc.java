package br.edu.ufape.gobarber.doc;

import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.model.BarberSchedule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Tag(name = "Agenda do Barbeiro", description = "API para gerenciamento de bloqueios de agenda, férias e disponibilidade")
public interface BarberScheduleControllerDoc {

    @Operation(summary = "Criar bloqueio", description = "Cria um bloqueio de horário na agenda do barbeiro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bloqueio criado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Barbeiro não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito com bloqueio existente")
    })
    ResponseEntity<BarberSchedule> createBlock(
            @Parameter(description = "ID do barbeiro") Long barberId,
            @Parameter(description = "Tipo de bloqueio") BarberSchedule.ScheduleType type,
            @Parameter(description = "Data/hora início") LocalDateTime startDateTime,
            @Parameter(description = "Data/hora fim") LocalDateTime endDateTime,
            @Parameter(description = "Motivo") String reason,
            @Parameter(description = "Se é recorrente") boolean recurring,
            @Parameter(description = "Dia da semana (se recorrente)") DayOfWeek dayOfWeek);

    @Operation(summary = "Registrar férias", description = "Registra período de férias do barbeiro")
    ResponseEntity<BarberSchedule> createVacation(
            Long barberId, LocalDateTime startDate, LocalDateTime endDate, String reason);

    @Operation(summary = "Registrar folga", description = "Registra um dia de folga do barbeiro")
    ResponseEntity<BarberSchedule> createDayOff(Long barberId, LocalDateTime date, String reason);

    @Operation(summary = "Horário de almoço recorrente", description = "Configura horário de almoço semanal")
    ResponseEntity<BarberSchedule> createRecurringLunchBreak(
            Long barberId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime);

    @Operation(summary = "Bloqueios do barbeiro", description = "Lista bloqueios de um barbeiro no período")
    ResponseEntity<List<BarberSchedule>> getBarberScheduleBlocks(
            @Parameter(description = "ID do barbeiro") Long barberId,
            LocalDateTime startDate, LocalDateTime endDate);

    @Operation(summary = "Férias do barbeiro", description = "Lista todas as férias registradas do barbeiro")
    ResponseEntity<List<BarberSchedule>> getBarberVacations(Long barberId);

    @Operation(summary = "Bloqueios recorrentes", description = "Lista bloqueios recorrentes do barbeiro")
    ResponseEntity<List<BarberSchedule>> getRecurringSchedules(Long barberId);

    @Operation(summary = "Verificar disponibilidade", description = "Verifica se barbeiro está disponível no horário")
    @ApiResponse(responseCode = "200", description = "true = disponível, false = indisponível")
    ResponseEntity<Boolean> checkAvailability(Long barberId, LocalDateTime startTime, LocalDateTime endTime);

    @Operation(summary = "Horários disponíveis", description = "Lista slots disponíveis do barbeiro no dia")
    ResponseEntity<List<LocalDateTime>> getAvailableSlots(Long barberId, String date, int slotDurationMinutes);

    @Operation(summary = "Barbeiros disponíveis", description = "Lista barbeiros disponíveis para um horário")
    ResponseEntity<List<Barber>> getAvailableBarbers(LocalDateTime startTime, LocalDateTime endTime);

    @Operation(summary = "Atualizar bloqueio", description = "Atualiza um bloqueio existente")
    ResponseEntity<BarberSchedule> updateBlock(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime, String reason);

    @Operation(summary = "Excluir bloqueio", description = "Remove um bloqueio da agenda")
    ResponseEntity<Void> deleteBlock(@Parameter(description = "ID do bloqueio") Long id);

    @Operation(summary = "Desativar bloqueio", description = "Desativa um bloqueio sem removê-lo")
    ResponseEntity<Void> deactivateBlock(Long id);

    @Operation(summary = "Dias de férias", description = "Conta dias de férias do barbeiro no ano")
    ResponseEntity<Map<String, Object>> getVacationDays(Long barberId, int year);
}
