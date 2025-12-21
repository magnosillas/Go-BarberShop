package br.edu.ufape.gobarber.controller.response;

import br.edu.ufape.gobarber.model.Services;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * DTO de resposta para serviços.
 * Segue o padrão da arquitetura base com construtor que recebe Model.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicesResponse {

    private Integer id;
    private String name;
    private String description;
    private double value;
    private LocalTime time;

    /**
     * Construtor que converte uma entidade Services para o DTO de resposta.
     * 
     * @param service Entidade Services a ser convertida
     */
    public ServicesResponse(Services service) {
        this.id = service.getId();
        this.name = service.getName();
        this.description = service.getDescription();
        this.value = service.getValue();
        this.time = service.getTime();
    }
}
