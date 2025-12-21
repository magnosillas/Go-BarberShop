package br.edu.ufape.gobarber.controller.request;

import br.edu.ufape.gobarber.model.Services;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalTime;

/**
 * DTO de requisição para criação e atualização de serviços.
 * Segue o padrão da arquitetura base com método toModel().
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicesRequest {

    @NotBlank(message = "Nome do serviço é obrigatório")
    private String name;
    
    private String description;
    
    @Positive(message = "Valor deve ser positivo")
    private double value;
    
    @NotNull(message = "Tempo de duração é obrigatório")
    private LocalTime time;

    /**
     * Converte o DTO de requisição para a entidade Services.
     * 
     * @return Nova instância de Services com os dados do request
     */
    public Services toModel() {
        Services service = new Services();
        service.setName(this.name);
        service.setDescription(this.description);
        service.setValue(this.value);
        service.setTime(this.time);
        return service;
    }

    /**
     * Atualiza uma entidade Services existente com os dados do request.
     * 
     * @param service Entidade existente a ser atualizada
     */
    public void updateModel(Services service) {
        service.setName(this.name);
        service.setDescription(this.description);
        service.setValue(this.value);
        service.setTime(this.time);
    }
}
