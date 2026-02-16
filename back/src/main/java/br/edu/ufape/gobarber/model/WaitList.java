package br.edu.ufape.gobarber.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade WaitList - Lista de espera para agendamentos.
 * Mapeado para corresponder à tabela wait_list no banco.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "wait_list")
public class WaitList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_wait_list")
    private Long idWaitList;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "preferred_barber_id")
    private Barber barber;

    @Column(name = "desired_time", nullable = false)
    private LocalDateTime desiredTime;

    @Column(name = "desired_duration")
    private Integer desiredDuration;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 20)
    private WaitListPriority priority = WaitListPriority.NORMAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private WaitListStatus status = WaitListStatus.WAITING;

    @Column(name = "position")
    private Integer position;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "notified")
    private Boolean notified = false;

    @Column(name = "notified_at")
    private LocalDateTime notifiedAt;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.expirationTime == null) {
            this.expirationTime = LocalDateTime.now().plusDays(7);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Notifica o cliente sobre disponibilidade.
     */
    public void notifyAvailability() {
        this.notified = true;
        this.notifiedAt = LocalDateTime.now();
        this.status = WaitListStatus.NOTIFIED;
    }

    /**
     * Converte para agendamento.
     */
    public void convertToAppointment() {
        this.status = WaitListStatus.CONVERTED;
    }

    // Enums
    public enum WaitListStatus {
        WAITING,
        NOTIFIED,
        CONVERTED,
        EXPIRED,
        CANCELLED
    }

    public enum WaitListPriority {
        LOW,
        NORMAL,
        HIGH,
        URGENT
    }
}
