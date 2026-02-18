package br.edu.ufape.gobarber.dto.appointment;

import br.edu.ufape.gobarber.dto.barber.BarberWithServiceDTO;
import br.edu.ufape.gobarber.model.Services;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AppointmentDTO {

    private Integer id;

    private String clientName;

    private String clientNumber;

    private BarberWithServiceDTO barber;

    private Set<Services> serviceType;

    private String startTime;

    private String endTime;

    private Double totalPrice;

    private String status;

    private String rejectionReason;

    private Long clientId;
}
