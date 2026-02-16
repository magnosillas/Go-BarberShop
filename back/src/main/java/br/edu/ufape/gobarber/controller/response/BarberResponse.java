package br.edu.ufape.gobarber.controller.response;

import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.model.Services;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO de resposta para barbeiros.
 * Segue o padrão da arquitetura base com construtor que recebe Model.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BarberResponse {

    private Integer idBarber;
    private String name;
    private String cpf;
    private AddressResponse address;
    private double salary;
    private LocalDate admissionDate;
    private Integer workload;
    private String contato;
    private LocalTime start;
    private LocalTime end;
    private Set<ServicesResponse> services;

    /**
     * Construtor que converte uma entidade Barber para o DTO de resposta.
     * 
     * @param barber Entidade Barber a ser convertida
     */
    public BarberResponse(Barber barber) {
        this.idBarber = barber.getIdBarber();
        this.name = barber.getName();
        this.cpf = barber.getCpf();
        
        if (barber.getAddress() != null) {
            this.address = new AddressResponse(barber.getAddress());
        }
        
        this.salary = barber.getSalary();
        this.admissionDate = barber.getAdmissionDate();
        this.workload = barber.getWorkload();
        this.contato = barber.getContato();
        this.start = barber.getStart();
        this.end = barber.getEnd();
        
        if (barber.getServices() != null) {
            this.services = barber.getServices().stream()
                    .map(ServicesResponse::new)
                    .collect(Collectors.toSet());
        }
    }
}
