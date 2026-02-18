package br.edu.ufape.gobarber.dto.notification;

import br.edu.ufape.gobarber.model.Notification;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO para transferência de dados de notificação.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {

    private Long idNotification;
    private Long clientId;
    private String clientName;
    private Long appointmentId;
    private Notification.NotificationType type;
    private Notification.NotificationChannel channel;
    private String title;
    private String message;
    private Boolean read;
    private LocalDateTime readAt;
    private LocalDateTime scheduledFor;
    private LocalDateTime sentAt;
    private Notification.NotificationStatus status;
    private LocalDateTime createdAt;

    /**
     * Converte uma entidade Notification para DTO.
     */
    public static NotificationDTO fromEntity(Notification notification) {
        if (notification == null) {
            return null;
        }

        NotificationDTOBuilder builder = NotificationDTO.builder()
                .idNotification(notification.getIdNotification())
                .type(notification.getType())
                .channel(notification.getChannel())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .read(notification.getReadAt() != null)
                .readAt(notification.getReadAt())
                .scheduledFor(notification.getScheduledFor())
                .sentAt(notification.getSentAt())
                .status(notification.getStatus())
                .createdAt(notification.getCreatedAt());

        if (notification.getClient() != null) {
            builder.clientId(notification.getClient().getIdClient())
                   .clientName(notification.getClient().getName());
        }

        if (notification.getAppointment() != null) {
            builder.appointmentId(notification.getAppointment().getId().longValue());
        }

        return builder.build();
    }
}
