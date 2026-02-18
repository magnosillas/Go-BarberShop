package br.edu.ufape.gobarber.model;

import br.edu.ufape.gobarber.model.login.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade Client - Representa um cliente da barbearia.
 * Permite gestão completa de clientes com histórico, preferências e fidelidade.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "client")
public class Client {
    public String getName() {
        return this.name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    private Long idClient;

    @Column(name = "name_client", nullable = false)
    private String name;

    @Column(name = "email_client", unique = true)
    private String email;

    @Column(name = "phone_client", nullable = false)
    private String phone;

    @Column(name = "cpf_client", unique = true)
    private String cpf;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_address", referencedColumnName = "id_adress")
    private Address address;

    @Column(name = "profile_photo")
    private byte[] profilePhoto;

    @Column(name = "notes")
    private String notes;

    // Fidelidade
    @Column(name = "loyalty_points", columnDefinition = "INTEGER DEFAULT 0")
    private Integer loyaltyPoints = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "loyalty_tier", columnDefinition = "VARCHAR(20) DEFAULT 'BRONZE'")
    private LoyaltyTier loyaltyTier = LoyaltyTier.BRONZE;

    // Preferências
    @ManyToOne
    @JoinColumn(name = "preferred_barber_id")
    private Barber preferredBarber;

    @Column(name = "preferred_contact_method")
    @Enumerated(EnumType.STRING)
    private ContactMethod preferredContactMethod = ContactMethod.WHATSAPP;

    @Column(name = "receive_promotions", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean receivePromotions = true;

    @Column(name = "receive_reminders", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean receiveReminders = true;

    // Métricas
    @Column(name = "total_visits", columnDefinition = "INTEGER DEFAULT 0")
    private Integer totalVisits = 0;

    @Column(name = "total_spent", columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private Double totalSpent = 0.0;

    @Column(name = "last_visit")
    private LocalDateTime lastVisit;

    @Column(name = "first_visit")
    private LocalDateTime firstVisit;

    // Controle
    @Column(name = "active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.loyaltyPoints == null) this.loyaltyPoints = 0;
        if (this.totalVisits == null) this.totalVisits = 0;
        if (this.totalSpent == null) this.totalSpent = 0.0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Adiciona pontos de fidelidade e atualiza o tier se necessário.
     */
    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
        updateLoyaltyTier();
    }

    /**
     * Registra uma nova visita do cliente.
     */
    public void registerVisit(double amountSpent) {
        this.totalVisits++;
        this.totalSpent += amountSpent;
        this.lastVisit = LocalDateTime.now();
        if (this.firstVisit == null) {
            this.firstVisit = LocalDateTime.now();
        }
        // 1 ponto a cada R$10 gastos
        int pointsEarned = (int) (amountSpent / 10);
        addLoyaltyPoints(pointsEarned);
    }

    /**
     * Atualiza o tier de fidelidade baseado nos pontos.
     */
    private void updateLoyaltyTier() {
        if (loyaltyPoints >= 1000) {
            this.loyaltyTier = LoyaltyTier.DIAMOND;
        } else if (loyaltyPoints >= 500) {
            this.loyaltyTier = LoyaltyTier.PLATINUM;
        } else if (loyaltyPoints >= 200) {
            this.loyaltyTier = LoyaltyTier.GOLD;
        } else if (loyaltyPoints >= 50) {
            this.loyaltyTier = LoyaltyTier.SILVER;
        } else {
            this.loyaltyTier = LoyaltyTier.BRONZE;
        }
    }

    /**
     * Retorna o desconto baseado no tier de fidelidade.
     */
    public double getLoyaltyDiscount() {
        return switch (loyaltyTier) {
            case DIAMOND -> 0.15; // 15%
            case PLATINUM -> 0.10; // 10%
            case GOLD -> 0.07; // 7%
            case SILVER -> 0.05; // 5%
            case BRONZE -> 0.0; // 0%
        };
    }

    // Enums
    public enum Gender {
        MALE, FEMALE, OTHER, PREFER_NOT_TO_SAY
    }

    public enum LoyaltyTier {
        BRONZE, SILVER, GOLD, PLATINUM, DIAMOND
    }

    public enum ContactMethod {
        EMAIL, SMS, WHATSAPP, PHONE
    }
}
