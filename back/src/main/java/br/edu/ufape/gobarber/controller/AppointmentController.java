package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.dto.appointment.AppointmentCreateDTO;
import br.edu.ufape.gobarber.dto.appointment.AppointmentDTO;
import br.edu.ufape.gobarber.dto.appointment.AppointmentRejectDTO;
import br.edu.ufape.gobarber.dto.appointment.AppointmentRequestDTO;
import br.edu.ufape.gobarber.dto.page.PageAppointmentDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;

import br.edu.ufape.gobarber.service.AppointmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/appointments")
@Validated
@Slf4j
@Tag(name = "Agendamentos", description = "Operações de gerenciamento de agendamentos")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Criar novo agendamento
    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentCreateDTO appointment)
            throws DataBaseException {
        AppointmentDTO savedAppointment = appointmentService.saveAppointment(appointment);
        return ResponseEntity.ok(savedAppointment);
    }

    // Atualizar agendamento existente
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Integer id,
            @RequestBody AppointmentCreateDTO appointment) throws DataBaseException {
        AppointmentDTO appointmentDTO = appointmentService.updateAppointment(id, appointment);
        return new ResponseEntity<>(appointmentDTO, HttpStatus.OK);
    }

    // Obter agendamento por ID
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Integer id) throws DataBaseException {
        AppointmentDTO appointment = appointmentService.getAppointmentById(id);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    // Obter todos os agendamentos
    @GetMapping
    public ResponseEntity<PageAppointmentDTO> getAllAppointments(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer siz) {
        PageAppointmentDTO appointments = appointmentService.getAllAppointments(page, siz);
        return ResponseEntity.ok(appointments);
    }

    // Obter agendamentos por barbeiro
    @GetMapping("/barber/{barberId}")
    public ResponseEntity<PageAppointmentDTO> getAppointmentsByBarber(@PathVariable Integer barberId,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer siz) throws DataBaseException {
        PageAppointmentDTO appointments = appointmentService.getAppointmentsByBarber(barberId, page, siz);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/history/barber")
    public ResponseEntity<PageAppointmentDTO> getHistory(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer siz,
            @RequestParam(value = "barberId") Integer barberId) throws DataBaseException {

        PageAppointmentDTO appointmentDTO = appointmentService.getHistoryByBarber(page, siz, barberId);
        return new ResponseEntity<>(appointmentDTO, HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<PageAppointmentDTO> getHistoryFromToken(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer siz,
            HttpServletRequest request) throws DataBaseException {

        PageAppointmentDTO appointmentDTO = appointmentService.getHistoryFromToken(page, siz, request);
        return new ResponseEntity<>(appointmentDTO, HttpStatus.OK);
    }

    @GetMapping("/future")
    public ResponseEntity<PageAppointmentDTO> getFutureAppointments(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer siz) {

        PageAppointmentDTO appointmentDTO = appointmentService.getFutureAppointments(page, siz);
        return new ResponseEntity<>(appointmentDTO, HttpStatus.OK);
    }

    @GetMapping("/future/barber")
    public ResponseEntity<PageAppointmentDTO> getFutureAppointmentsByBarber(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer siz,
            @RequestParam(value = "barberId") Integer barberId)
            throws DataBaseException {

        PageAppointmentDTO appointmentDTO = appointmentService.getFutureAppointments(page, siz, barberId);
        return new ResponseEntity<>(appointmentDTO, HttpStatus.OK);
    }

    @GetMapping("/future/barber/own")
    public ResponseEntity<PageAppointmentDTO> getFutureAppointmentsByBarber(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer siz,
            HttpServletRequest request) throws DataBaseException {

        PageAppointmentDTO appointmentDTO = appointmentService.getFutureAppointments(page, siz, request);
        return new ResponseEntity<>(appointmentDTO, HttpStatus.OK);
    }

    // Excluir agendamento por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Integer id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok().build();
    }

    // ==================== Workflow de Aprovação ====================

    // Aprovar agendamento pendente
    @PostMapping("/{id}/approve")
    public ResponseEntity<AppointmentDTO> approveAppointment(@PathVariable Integer id) throws DataBaseException {
        AppointmentDTO appointmentDTO = appointmentService.approveAppointment(id);
        return ResponseEntity.ok(appointmentDTO);
    }

    // Rejeitar agendamento pendente
    @PostMapping("/{id}/reject")
    public ResponseEntity<AppointmentDTO> rejectAppointment(
            @PathVariable Integer id,
            @RequestBody @Valid AppointmentRejectDTO rejectDTO) throws DataBaseException {
        AppointmentDTO appointmentDTO = appointmentService.rejectAppointment(id, rejectDTO.getReason());
        return ResponseEntity.ok(appointmentDTO);
    }

    // Listar agendamentos pendentes de aprovação
    @GetMapping("/pending")
    public ResponseEntity<PageAppointmentDTO> getPendingAppointments(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        PageAppointmentDTO appointments = appointmentService.getPendingAppointments(page, size);
        return ResponseEntity.ok(appointments);
    }

    // ==================== Endpoints do Cliente ====================

    // Cliente solicita agendamento
    @PostMapping("/request")
    public ResponseEntity<AppointmentDTO> requestAppointment(
            @RequestBody @Valid AppointmentRequestDTO requestDTO,
            HttpServletRequest request) throws DataBaseException {
        AppointmentDTO appointmentDTO = appointmentService.requestAppointment(requestDTO, request);
        return new ResponseEntity<>(appointmentDTO, HttpStatus.CREATED);
    }

    // Cliente lista seus agendamentos
    @GetMapping("/my")
    public ResponseEntity<PageAppointmentDTO> getMyAppointments(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            HttpServletRequest request) throws DataBaseException {
        PageAppointmentDTO appointments = appointmentService.getMyAppointments(page, size, request);
        return ResponseEntity.ok(appointments);
    }

    // Cliente cancela seu agendamento
    @PostMapping("/my/{id}/cancel")
    public ResponseEntity<AppointmentDTO> cancelMyAppointment(
            @PathVariable Integer id,
            HttpServletRequest request) throws DataBaseException {
        AppointmentDTO appointmentDTO = appointmentService.cancelMyAppointment(id, request);
        return ResponseEntity.ok(appointmentDTO);
    }
}
