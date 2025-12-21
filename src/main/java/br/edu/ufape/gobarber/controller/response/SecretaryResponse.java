package br.edu.ufape.gobarber.controller.response;

import br.edu.ufape.gobarber.model.Secretary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO de resposta para secretárias.
 * Segue o padrão da arquitetura base com construtor que recebe Model.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecretaryResponse {

    private Integer idSecretary;
    private String name;
    private String cpf;
    private AddressResponse address;
    private double salary;
    private LocalDate admissionDate;
    private Integer workload;
    private String contact;
    private LocalTime start;
    private LocalTime end;

    /**
     * Construtor que converte uma entidade Secretary para o DTO de resposta.
     * 
     * @param secretary Entidade Secretary a ser convertida
     */
    public SecretaryResponse(Secretary secretary) {
        this.idSecretary = secretary.getIdSecretary();
        this.name = secretary.getName();
        this.cpf = secretary.getCpf();
        
        if (secretary.getAddress() != null) {
            this.address = new AddressResponse(secretary.getAddress());
        }
        
        this.salary = secretary.getSalary();
        this.admissionDate = secretary.getAdmissionDate();
        this.workload = secretary.getWorkload();
        this.contact = secretary.getContact();
        this.start = secretary.getStart();
        this.end = secretary.getEnd();
    }
}
