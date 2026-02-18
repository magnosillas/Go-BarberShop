package br.edu.ufape.gobarber.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointment")
public class Appointment {

    public enum AppointmentStatus {
        PENDING_APPROVAL,
        CONFIRMED,
        REJECTED,
        CANCELLED,
        COMPLETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_appointment")
    private Integer id;

    @Column(name="name_client")
    private String clientName;

    @Column(name="number_client")
    private String clientNumber;

    @ManyToOne
    @JoinColumn(name="id_barber")
    private Barber barber;

    @ManyToMany
    @JoinTable(
            name = "appointment_service",
            joinColumns = @JoinColumn(name = "id_appointment"),
            inverseJoinColumns = @JoinColumn(name = "id_service")
    )
    private Set<Services> serviceType;

    @Column(name="start_time")
    private LocalDateTime startTime;

    @Column(name="end_time")
    private LocalDateTime endTime;

    @Column(name = "total_price")
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus status = AppointmentStatus.CONFIRMED;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
