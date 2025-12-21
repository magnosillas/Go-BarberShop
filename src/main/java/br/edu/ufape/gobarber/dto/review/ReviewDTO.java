package br.edu.ufape.gobarber.dto.review;

import br.edu.ufape.gobarber.model.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

    private Long idReview;
    private Long clientId;
    private String clientName;
    private Integer barberId;
    private String barberName;
    private Long appointmentId;
    private Integer rating;
    private String comment;
    private Integer serviceRating;
    private Integer punctualityRating;
    private Integer cleanlinessRating;
    private Integer valueRating;
    private Double averageRating;
    private Boolean wouldRecommend;
    private String reply;
    private LocalDateTime replyDate;
    private Boolean isVisible;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReviewDTO fromEntity(Review review) {
        ReviewDTOBuilder builder = ReviewDTO.builder()
                .idReview(review.getIdReview())
                .rating(review.getRating())
                .comment(review.getComment())
                .serviceRating(review.getServiceRating())
                .punctualityRating(review.getPunctualityRating())
                .cleanlinessRating(review.getCleanlinessRating())
                .valueRating(review.getValueRating())
                .averageRating(review.getAverageRating())
                .wouldRecommend(review.getWouldRecommend())
                .reply(review.getReply())
                .replyDate(review.getReplyDate())
                .isVisible(review.getIsVisible())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt());

        if (review.getClient() != null) {
            builder.clientId(review.getClient().getIdClient());
            builder.clientName(review.getClient().getName());
        }

        if (review.getBarber() != null) {
            builder.barberId(review.getBarber().getIdBarber());
            builder.barberName(review.getBarber().getName());
        }

        if (review.getAppointment() != null) {
            builder.appointmentId(review.getAppointment().getId().longValue());
        }

        return builder.build();
    }
}
