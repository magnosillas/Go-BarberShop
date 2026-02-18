package br.edu.ufape.gobarber.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AppointmentRejectDTO {

    @NotBlank(message = "O motivo da rejeição é obrigatório")
    private String reason;
}
