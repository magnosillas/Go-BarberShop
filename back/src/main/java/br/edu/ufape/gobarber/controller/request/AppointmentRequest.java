package br.edu.ufape.gobarber.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de requisição para criação e atualização de agendamentos.
 * Segue o padrão da arquitetura base com método toModel().
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest {

    @NotBlank(message = "Nome do cliente é obrigatório")
    private String clientName;
    
    @NotBlank(message = "Número do cliente é obrigatório")
    private String clientNumber;
    
    @NotNull(message = "ID do barbeiro é obrigatório")
    private Integer barberId;
    
    @NotEmpty(message = "Pelo menos um serviço deve ser selecionado")
    private List<Integer> serviceTypeIds;
    
    @NotNull(message = "Horário de início é obrigatório")
    private LocalDateTime startTime;
}
