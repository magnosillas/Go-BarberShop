package br.edu.ufape.gobarber.doc;

import br.edu.ufape.gobarber.model.WaitList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Tag(name = "Lista de Espera", description = "API para gerenciamento de lista de espera")
public interface WaitListControllerDoc {

    @Operation(summary = "Adicionar à lista", description = "Adiciona um cliente à lista de espera")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente adicionado à lista"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    ResponseEntity<WaitList> addToWaitList(
            @Parameter(description = "ID do cliente") Long clientId,
            @Parameter(description = "Horário desejado") LocalDateTime desiredTime,
            @Parameter(description = "Duração desejada em minutos") Integer desiredDuration,
            @Parameter(description = "ID do barbeiro preferido") Long preferredBarberId,
            @Parameter(description = "Prioridade") WaitList.WaitListPriority priority,
            @Parameter(description = "Observações") String notes);

    @Operation(summary = "Lista de espera ativa", description = "Retorna a lista de espera ativa ordenada por posição")
    ResponseEntity<List<WaitList>> getActiveWaitList();

    @Operation(summary = "Lista por barbeiro", description = "Retorna entradas com preferência por um barbeiro")
    ResponseEntity<List<WaitList>> getWaitListByBarber(@Parameter(description = "ID do barbeiro") Long barberId);

    @Operation(summary = "Histórico do cliente", description = "Retorna histórico de lista de espera do cliente")
    ResponseEntity<List<WaitList>> getClientHistory(@Parameter(description = "ID do cliente") Long clientId);

    @Operation(summary = "Entrada ativa do cliente", description = "Retorna entrada ativa do cliente na lista")
    ResponseEntity<WaitList> getClientActiveEntry(@Parameter(description = "ID do cliente") Long clientId);

    @Operation(summary = "Posição do cliente", description = "Retorna a posição do cliente na lista de espera")
    ResponseEntity<Integer> getClientPosition(@Parameter(description = "ID do cliente") Long clientId);

    @Operation(summary = "Cliente aceitou", description = "Registra que o cliente aceitou a oferta de horário")
    ResponseEntity<Void> clientAccepted(@Parameter(description = "ID da entrada") Long id);

    @Operation(summary = "Cliente recusou", description = "Registra que o cliente recusou a oferta de horário")
    ResponseEntity<Void> clientDeclined(@Parameter(description = "ID da entrada") Long id);

    @Operation(summary = "Cliente confirmou", description = "Registra que o cliente confirmou o agendamento")
    ResponseEntity<Void> clientConfirmed(@Parameter(description = "ID da entrada") Long id);

    @Operation(summary = "Cancelar entrada", description = "Cancela uma entrada na lista de espera")
    ResponseEntity<Void> cancel(@Parameter(description = "ID da entrada") Long id);

    @Operation(summary = "Atualizar posição", description = "Altera a posição de uma entrada na lista (admin)")
    ResponseEntity<Void> updatePosition(Long id, Integer newPosition);

    @Operation(summary = "Atualizar prioridade", description = "Altera a prioridade de uma entrada (admin)")
    ResponseEntity<Void> updatePriority(Long id, WaitList.WaitListPriority priority);

    @Operation(summary = "Processar disponibilidade", description = "Processa lista quando há disponibilidade")
    ResponseEntity<Void> processAvailability(Long barberId, LocalDateTime availableTime, Integer duration);

    @Operation(summary = "Processar expirados", description = "Processa entradas expiradas")
    ResponseEntity<Void> processExpired();

    @Operation(summary = "Estatísticas", description = "Retorna estatísticas da lista de espera")
    ResponseEntity<Map<String, Object>> getStats();
}
