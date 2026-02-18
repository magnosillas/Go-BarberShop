package br.edu.ufape.gobarber.dto.client;

import br.edu.ufape.gobarber.dto.address.AddressDTO;
import br.edu.ufape.gobarber.model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {

    private Long idClient;
    private String name;
    private String email;
    private String phone;
    private String cpf;
    private LocalDate birthDate;
    private Client.Gender gender;
    private AddressDTO address;
    private String notes;

    // Fidelidade
    private Integer loyaltyPoints;
    private Client.LoyaltyTier loyaltyTier;
    private Double loyaltyDiscount;

    // Preferências
    private Integer preferredBarberId;
    private String preferredBarberName;
    private Client.ContactMethod preferredContactMethod;
    private Boolean receivePromotions;
    private Boolean receiveReminders;

    // Métricas
    private Integer totalVisits;
    private Double totalSpent;
    private LocalDateTime lastVisit;
    private LocalDateTime firstVisit;

    // Controle
    private Boolean active;
    private LocalDateTime createdAt;

    public static ClientDTO fromEntity(Client client) {
        ClientDTOBuilder builder = ClientDTO.builder()
                .idClient(client.getIdClient())
                .name(client.getName())
                .email(client.getEmail())
                .phone(client.getPhone())
                .cpf(client.getCpf())
                .birthDate(client.getBirthDate())
                .gender(client.getGender())
                .notes(client.getNotes())
                .loyaltyPoints(client.getLoyaltyPoints())
                .loyaltyTier(client.getLoyaltyTier())
                .loyaltyDiscount(client.getLoyaltyDiscount())
                .preferredContactMethod(client.getPreferredContactMethod())
                .receivePromotions(client.getReceivePromotions())
                .receiveReminders(client.getReceiveReminders())
                .totalVisits(client.getTotalVisits())
                .totalSpent(client.getTotalSpent())
                .lastVisit(client.getLastVisit())
                .firstVisit(client.getFirstVisit())
                .active(client.getActive())
                .createdAt(client.getCreatedAt());

        if (client.getAddress() != null) {
            builder.address(AddressDTO.fromEntity(client.getAddress()));
        }

        if (client.getPreferredBarber() != null) {
            builder.preferredBarberId(client.getPreferredBarber().getIdBarber());
            builder.preferredBarberName(client.getPreferredBarber().getName());
        }

        return builder.build();
    }
}
