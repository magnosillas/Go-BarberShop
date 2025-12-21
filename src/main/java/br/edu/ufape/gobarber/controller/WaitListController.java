package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.dto.waitlist.WaitListDTO;
import br.edu.ufape.gobarber.model.WaitList;
import br.edu.ufape.gobarber.service.WaitListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/waitlist")
@RequiredArgsConstructor
@Tag(name = "Lista de Espera", description = "API para gerenciamento de lista de espera")
public class WaitListController {

    private final WaitListService waitListService;

    // === Adicionar à Lista ===

    @PostMapping
    public ResponseEntity<WaitList> addToWaitList(
            @RequestParam Long clientId,
            @RequestParam(required = false) Long barberId,
            @RequestParam(required = false) Integer serviceId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime preferredDate,
            @RequestParam(required = false) Boolean flexibleDate,
            @RequestParam(required = false) Integer priority,
            @RequestParam(required = false) String notes) {
        
        WaitList entry = waitListService.addToWaitList(
                clientId, barberId, serviceId, preferredDate, flexibleDate, priority, notes);
        return new ResponseEntity<>(entry, HttpStatus.CREATED);
    }

    // === Consultas ===

    @GetMapping
    public ResponseEntity<List<WaitList>> getActiveWaitList() {
        return ResponseEntity.ok(waitListService.getActiveWaitList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WaitListDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(waitListService.getWaitListById(id));
    }

    @GetMapping("/barber/{barberId}")
    public ResponseEntity<Page<WaitList>> getWaitListByBarber(
            @PathVariable Long barberId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(waitListService.getWaitListByBarber(barberId, pageable));
    }

    @GetMapping("/barber/{barberId}/waiting")
    public ResponseEntity<List<WaitList>> getWaitingByBarber(@PathVariable Long barberId) {
        return ResponseEntity.ok(waitListService.getWaitingByBarber(barberId));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<Page<WaitList>> getClientWaitList(
            @PathVariable Long clientId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(waitListService.getWaitListByClient(clientId, pageable));
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<WaitList>> getWaitingByService(@PathVariable Integer serviceId) {
        return ResponseEntity.ok(waitListService.getWaitingByService(serviceId));
    }

    // === Ações ===

    @PostMapping("/{id}/notify")
    public ResponseEntity<Void> notifyClient(@PathVariable Long id) {
        waitListService.notifyClientAboutAvailability(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/convert")
    public ResponseEntity<Void> convertToAppointment(@PathVariable Long id) {
        waitListService.convertToAppointment(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        waitListService.cancelWaitListEntry(id);
        return ResponseEntity.noContent().build();
    }

    // === Admin ===

    @PutMapping("/{id}/priority")
    public ResponseEntity<WaitList> updatePriority(
            @PathVariable Long id,
            @RequestParam Integer priority) {
        return ResponseEntity.ok(waitListService.updatePriority(id, priority));
    }

    @PutMapping("/{id}/notes")
    public ResponseEntity<WaitList> updateNotes(
            @PathVariable Long id,
            @RequestBody String notes) {
        return ResponseEntity.ok(waitListService.updateNotes(id, notes));
    }

    @PostMapping("/process-expired")
    public ResponseEntity<Void> processExpired() {
        waitListService.processExpiredEntries();
        return ResponseEntity.ok().build();
    }

    // === Estatísticas ===

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Long totalWaiting = waitListService.getTotalWaitingCount();
        List<Object[]> byBarber = waitListService.getWaitingGroupByBarber();
        
        return ResponseEntity.ok(Map.of(
                "totalWaiting", totalWaiting,
                "byBarber", byBarber
        ));
    }

    @GetMapping("/stats/barber/{barberId}")
    public ResponseEntity<Long> getWaitingCountByBarber(@PathVariable Long barberId) {
        return ResponseEntity.ok(waitListService.getWaitingCountByBarber(barberId));
    }
}
