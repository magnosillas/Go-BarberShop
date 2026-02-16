package br.edu.ufape.gobarber.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade Review - Avaliações dos clientes sobre atendimentos.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_review")
    private Long idReview;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "barber_id", nullable = false)
    private Barber barber;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(name = "rating", nullable = false)
    private Integer rating; // 1-5 estrelas

    @Column(name = "comment", length = 1000)
    private String comment;

    @Column(name = "service_rating")
    private Integer serviceRating; // Avaliação do serviço

    @Column(name = "punctuality_rating")
    private Integer punctualityRating; // Avaliação da pontualidade

    @Column(name = "cleanliness_rating")
    private Integer cleanlinessRating; // Avaliação da limpeza

    @Column(name = "value_rating")
    private Integer valueRating; // Avaliação do custo-benefício

    @Column(name = "would_recommend", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean wouldRecommend = true;

    @Column(name = "reply")
    private String reply; // Resposta do barbeiro/estabelecimento

    @Column(name = "reply_date")
    private LocalDateTime replyDate;

    @Column(name = "is_visible", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isVisible = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Calcula a média geral da avaliação.
     */
    public Double getAverageRating() {
        int count = 1;
        double sum = rating;
        
        if (serviceRating != null) {
            sum += serviceRating;
            count++;
        }
        if (punctualityRating != null) {
            sum += punctualityRating;
            count++;
        }
        if (cleanlinessRating != null) {
            sum += cleanlinessRating;
            count++;
        }
        if (valueRating != null) {
            sum += valueRating;
            count++;
        }
        
        return sum / count;
    }

    /**
     * Adiciona resposta à avaliação.
     */
    public void addReply(String replyText) {
        this.reply = replyText;
        this.replyDate = LocalDateTime.now();
    }
}
