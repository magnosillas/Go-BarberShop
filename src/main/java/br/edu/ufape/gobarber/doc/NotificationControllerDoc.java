package br.edu.ufape.gobarber.doc;

import br.edu.ufape.gobarber.model.Notification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Tag(name = "Notificações", description = "API para gerenciamento de notificações")
public interface NotificationControllerDoc {

    @Operation(summary = "Notificações do cliente", description = "Lista todas as notificações de um cliente")
    @ApiResponse(responseCode = "200", description = "Lista de notificações retornada")
    ResponseEntity<Page<Notification>> getClientNotifications(
            @Parameter(description = "ID do cliente") Long clientId, Pageable pageable);

    @Operation(summary = "Notificações não lidas", description = "Lista notificações não lidas de um cliente")
    ResponseEntity<List<Notification>> getUnreadNotifications(
            @Parameter(description = "ID do cliente") Long clientId);

    @Operation(summary = "Contador de não lidas", description = "Retorna quantidade de notificações não lidas")
    ResponseEntity<Long> getUnreadCount(@Parameter(description = "ID do cliente") Long clientId);

    @Operation(summary = "Marcar como lida", description = "Marca uma notificação como lida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificação marcada como lida"),
            @ApiResponse(responseCode = "404", description = "Notificação não encontrada")
    })
    ResponseEntity<Void> markAsRead(@Parameter(description = "ID da notificação") Long id);

    @Operation(summary = "Marcar todas como lidas", description = "Marca todas as notificações do cliente como lidas")
    ResponseEntity<Void> markAllAsRead(@Parameter(description = "ID do cliente") Long clientId);

    @Operation(summary = "Notificações pendentes", description = "Lista notificações aguardando envio (admin)")
    ResponseEntity<List<Notification>> getPendingNotifications();

    @Operation(summary = "Notificações com falha", description = "Lista notificações que falharam no envio (admin)")
    ResponseEntity<List<Notification>> getFailedNotifications();

    @Operation(summary = "Reenviar notificação", description = "Reenvia uma notificação que falhou")
    ResponseEntity<Void> resendNotification(@Parameter(description = "ID da notificação") Long id);

    @Operation(summary = "Estatísticas", description = "Retorna estatísticas de notificações")
    ResponseEntity<Map<String, Object>> getStats();
}
