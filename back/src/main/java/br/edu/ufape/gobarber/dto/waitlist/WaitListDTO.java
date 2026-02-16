package br.edu.ufape.gobarber.dto.waitlist;

import br.edu.ufape.gobarber.model.WaitList;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO para transferência de dados da lista de espera.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaitListDTO {

    private Long idWaitList;
    private Long clientId;
    private String clientName;
    private Long barberId;
    private String barberName;
    private LocalDateTime desiredTime;
    private Integer desiredDuration;
    private WaitList.WaitListPriority priority;
    private WaitList.WaitListStatus status;
    private Integer position;
    private String notes;
    private Boolean notified;
    private LocalDateTime notifiedAt;
    private LocalDateTime expirationTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Converte uma entidade WaitList para DTO.
     */
    public static WaitListDTO fromEntity(WaitList waitList) {
        if (waitList == null) {
            return null;
        }

        WaitListDTOBuilder builder = WaitListDTO.builder()
                .idWaitList(waitList.getIdWaitList())
                .desiredTime(waitList.getDesiredTime())
                .desiredDuration(waitList.getDesiredDuration())
                .priority(waitList.getPriority())
                .status(waitList.getStatus())
                .position(waitList.getPosition())
                .notes(waitList.getNotes())
                .notified(waitList.getNotified())
                .notifiedAt(waitList.getNotifiedAt())
                .expirationTime(waitList.getExpirationTime())
                .createdAt(waitList.getCreatedAt())
                .updatedAt(waitList.getUpdatedAt());

        if (waitList.getClient() != null) {
            builder.clientId(waitList.getClient().getIdClient())
                   .clientName(waitList.getClient().getName());
        }

        if (waitList.getBarber() != null) {
            builder.barberId(waitList.getBarber().getIdBarber().longValue())
                   .barberName(waitList.getBarber().getName());
        }

        return builder.build();
    }
}
