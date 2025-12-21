package br.edu.ufape.gobarber.controller.response;

import br.edu.ufape.gobarber.model.Appointment;
import br.edu.ufape.gobarber.model.Services;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * DTO de resposta para agendamentos.
 * Segue o padrão da arquitetura base com construtor que recebe Model.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {

    private Integer id;
    private String clientName;
    private String clientNumber;
    private BarberResponse barber;
    private Set<Services> serviceType;
    private String startTime;
    private String endTime;
    private Double totalPrice;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Construtor que converte uma entidade Appointment para o DTO de resposta.
     * 
     * @param appointment Entidade Appointment a ser convertida
     */
    public AppointmentResponse(Appointment appointment) {
        this.id = appointment.getId();
        this.clientName = appointment.getClientName();
        this.clientNumber = appointment.getClientNumber();
        
        if (appointment.getBarber() != null) {
            this.barber = new BarberResponse(appointment.getBarber());
        }
        
        this.serviceType = appointment.getServiceType();
        
        if (appointment.getStartTime() != null) {
            this.startTime = appointment.getStartTime().format(formatter);
        }
        
        if (appointment.getEndTime() != null) {
            this.endTime = appointment.getEndTime().format(formatter);
        }
        
        this.totalPrice = appointment.getTotalPrice();
    }
}
