package br.edu.ufape.gobarber.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade Notification - Sistema de notificações.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "notification")
public class Notification {
    public Long getIdNotification() {
        return this.idNotification;
    }

    public String getRecipientEmail() {
        return this.recipientEmail;
    }

    public String getTitle() {
        return this.title;
    }

    public String getMessage() {
        return this.message;
    }

    public String getRecipientPhone() {
        return this.recipientPhone;
    }

    public NotificationChannel getChannel() {
        return this.channel;
    }

    public NotificationStatus getStatus() {
        return this.status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification")
    private Long idNotification;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "barber_id")
    private Barber barber;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private NotificationChannel channel;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false, length = 2000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status = NotificationStatus.PENDING;

    @Column(name = "recipient_email")
    private String recipientEmail;

    @Column(name = "recipient_phone")
    private String recipientPhone;

    @Column(name = "scheduled_for")
    private LocalDateTime scheduledFor;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "retry_count", columnDefinition = "INTEGER DEFAULT 0")
    private Integer retryCount = 0;

    @Column(name = "external_message_id")
    private String externalId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Marca a notificação como enviada.
     */
    public void markAsSent(String externalId) {
        this.status = NotificationStatus.SENT;
        this.sentAt = LocalDateTime.now();
        this.externalId = externalId;
    }

    /**
     * Marca a notificação como lida.
     */
    public void markAsRead() {
        this.status = NotificationStatus.READ;
        this.readAt = LocalDateTime.now();
    }

    /**
     * Marca a notificação como falha.
     */
    public void markAsFailed(String errorMessage) {
        this.status = NotificationStatus.FAILED;
        this.errorMessage = errorMessage;
        this.retryCount++;
    }

    // Enums
    public enum NotificationType {
        APPOINTMENT_CONFIRMATION,   // Confirmação de agendamento
        APPOINTMENT_REMINDER,       // Lembrete de agendamento
        APPOINTMENT_CANCELLED,      // Agendamento cancelado
        APPOINTMENT_RESCHEDULED,    // Agendamento remarcado
        PROMOTION,                  // Promoção
        LOYALTY_POINTS,             // Pontos de fidelidade
        BIRTHDAY,                   // Aniversário
        WELCOME,                    // Boas-vindas
        PASSWORD_RESET,             // Reset de senha
        PAYMENT_CONFIRMATION,       // Confirmação de pagamento
        REVIEW_REQUEST              // Solicitação de avaliação
    }

    public enum NotificationChannel {
        EMAIL,
        SMS,
        WHATSAPP,
        PUSH
    }

    public enum NotificationStatus {
        PENDING,
        SCHEDULED,
        SENT,
        DELIVERED,
        READ,
        FAILED
    }
}
