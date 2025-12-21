package br.edu.ufape.gobarber.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade AuditLog - Registro de auditoria de ações do sistema.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_audit_log")
    private Long idAuditLog;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_email")
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private AuditAction action;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "request_uri")
    private String requestUri;

    @Column(name = "request_method")
    private String requestMethod;

    @Column(name = "response_status")
    private Integer responseStatus;

    @Column(name = "execution_time_ms")
    private Long executionTimeMs;

    @Column(name = "success", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean success = true;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Enumeração de tipos de ação para auditoria.
     */
    public enum AuditAction {
        LOGIN,
        LOGOUT,
        LOGIN_FAILED,
        CREATE,
        UPDATE,
        DELETE,
        VIEW,
        PAYMENT,
        REFUND,
        CANCEL,
        APPROVE,
        REJECT,
        EXPORT,
        IMPORT,
        PASSWORD_CHANGE,
        PERMISSION_CHANGE
    }

    /**
     * Builder estático para criar log de sucesso.
     */
    public static AuditLog success(AuditAction action, String entityType, Long entityId) {
        return AuditLog.builder()
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .success(true)
                .build();
    }

    /**
     * Builder estático para criar log de erro.
     */
    public static AuditLog error(AuditAction action, String entityType, String errorMessage) {
        return AuditLog.builder()
                .action(action)
                .entityType(entityType)
                .success(false)
                .errorMessage(errorMessage)
                .build();
    }
}
