package br.edu.ufape.gobarber.doc;

import br.edu.ufape.gobarber.dto.appointment.AppointmentDTO;
import br.edu.ufape.gobarber.dto.page.PageAppointmentDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Documentação Swagger para AppointmentController.
 * Define operações de agendamento.
 * 
 * @author GoBarber Team
 * @version 2.0
 */
@Tag(name = "Agendamentos", description = "Operações de gerenciamento de agendamentos")
public interface AppointmentControllerDoc {

    @Operation(summary = "Criar agendamento", description = "Cria um novo agendamento")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamento criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2025-12-20T10:30:00Z",
                                              "status": 400,
                                              "error": "Erro de validação",
                                              "message": "Um ou mais campos estão inválidos"
                                            }"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "406",
                    description = "Conflito de horário",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2025-12-20T10:30:00Z",
                                              "status": 406,
                                              "error": "Erro de agendamento",
                                              "message": "Horário não disponível para este barbeiro"
                                            }"""
                            )
                    )
            )
    })
    @PostMapping
    ResponseEntity<AppointmentDTO> createAppointment(@RequestBody Object appointment) throws DataBaseException;

    @Operation(summary = "Atualizar agendamento", description = "Atualiza um agendamento existente")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamento atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    })
    @PutMapping("/{id}")
    ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Integer id, @RequestBody Object appointment) throws DataBaseException;

    @Operation(summary = "Buscar agendamento por ID", description = "Retorna um agendamento específico")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamento encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    })
    @GetMapping("/{id}")
    ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Integer id) throws DataBaseException;

    @Operation(summary = "Listar agendamentos", description = "Retorna todos os agendamentos com paginação")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de agendamentos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageAppointmentDTO.class)
                    )
            )
    })
    @GetMapping
    ResponseEntity<PageAppointmentDTO> getAllAppointments(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size);

    @Operation(summary = "Listar agendamentos por barbeiro", description = "Retorna agendamentos de um barbeiro específico")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamentos do barbeiro",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageAppointmentDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Barbeiro não encontrado")
    })
    @GetMapping("/barber/{barberId}")
    ResponseEntity<PageAppointmentDTO> getAppointmentsByBarber(
            @PathVariable Integer barberId,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) throws DataBaseException;

    @Operation(summary = "Histórico por barbeiro", description = "Retorna histórico de agendamentos de um barbeiro")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Histórico de agendamentos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageAppointmentDTO.class)
                    )
            )
    })
    @GetMapping("/history/barber")
    ResponseEntity<PageAppointmentDTO> getHistory(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "barberId") Integer barberId) throws DataBaseException;

    @Operation(summary = "Histórico do barbeiro logado", description = "Retorna histórico de agendamentos do barbeiro autenticado")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Histórico de agendamentos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageAppointmentDTO.class)
                    )
            )
    })
    @GetMapping("/history")
    ResponseEntity<PageAppointmentDTO> getHistoryFromToken(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            HttpServletRequest request) throws DataBaseException;

    @Operation(summary = "Agendamentos futuros", description = "Retorna todos os agendamentos futuros")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamentos futuros",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageAppointmentDTO.class)
                    )
            )
    })
    @GetMapping("/future")
    ResponseEntity<PageAppointmentDTO> getFutureAppointments(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size);

    @Operation(summary = "Agendamentos futuros por barbeiro", description = "Retorna agendamentos futuros de um barbeiro")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamentos futuros do barbeiro",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageAppointmentDTO.class)
                    )
            )
    })
    @GetMapping("/future/barber")
    ResponseEntity<PageAppointmentDTO> getFutureAppointmentsByBarber(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "barberId") Integer barberId) throws DataBaseException;
}
