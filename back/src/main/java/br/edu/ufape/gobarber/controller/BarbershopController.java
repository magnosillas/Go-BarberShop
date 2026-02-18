package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.dto.barbershop.BarbershopCreateDTO;
import br.edu.ufape.gobarber.dto.barbershop.BarbershopDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.service.BarbershopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/barbershop")
@RequiredArgsConstructor
@Tag(name = "Barbearias", description = "Gerenciamento de barbearias")
public class BarbershopController {

    private final BarbershopService barbershopService;

    @Operation(summary = "Listar todas as barbearias")
    @GetMapping
    public ResponseEntity<List<BarbershopDTO>> getAllBarbershops() {
        return ResponseEntity.ok(barbershopService.getAllBarbershops());
    }

    @Operation(summary = "Listar barbearias ativas")
    @GetMapping("/active")
    public ResponseEntity<List<BarbershopDTO>> getActiveBarbershops() {
        return ResponseEntity.ok(barbershopService.getActiveBarbershops());
    }

    @Operation(summary = "Buscar barbearia por ID")
    @GetMapping("/{id}")
    public ResponseEntity<BarbershopDTO> getBarbershopById(@PathVariable Integer id) throws DataBaseException {
        return ResponseEntity.ok(barbershopService.getBarbershopById(id));
    }

    @Operation(summary = "Buscar barbearia por slug")
    @GetMapping("/slug/{slug}")
    public ResponseEntity<BarbershopDTO> getBarbershopBySlug(@PathVariable String slug) throws DataBaseException {
        return ResponseEntity.ok(barbershopService.getBarbershopBySlug(slug));
    }

    @Operation(summary = "Buscar barbearias por nome")
    @GetMapping("/search")
    public ResponseEntity<List<BarbershopDTO>> searchBarbershops(@RequestParam String name) {
        return ResponseEntity.ok(barbershopService.searchBarbershops(name));
    }

    @Operation(summary = "Listar barbearias de um cliente")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<BarbershopDTO>> getBarbershopsByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(barbershopService.getBarbershopsByClient(clientId));
    }

    @Operation(summary = "Criar nova barbearia")
    @PostMapping
    public ResponseEntity<BarbershopDTO> createBarbershop(@Valid @RequestBody BarbershopCreateDTO createDTO) throws DataBaseException {
        BarbershopDTO shop = barbershopService.createBarbershop(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(shop);
    }

    @Operation(summary = "Atualizar barbearia")
    @PutMapping("/{id}")
    public ResponseEntity<BarbershopDTO> updateBarbershop(
            @PathVariable Integer id,
            @Valid @RequestBody BarbershopCreateDTO updateDTO) throws DataBaseException {
        return ResponseEntity.ok(barbershopService.updateBarbershop(id, updateDTO));
    }

    @Operation(summary = "Excluir barbearia")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBarbershop(@PathVariable Integer id) throws DataBaseException {
        barbershopService.deleteBarbershop(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Ativar/desativar barbearia")
    @PostMapping("/{id}/toggle")
    public ResponseEntity<BarbershopDTO> toggleActive(@PathVariable Integer id) throws DataBaseException {
        return ResponseEntity.ok(barbershopService.toggleActive(id));
    }

    @Operation(summary = "Vincular cliente a barbearia")
    @PostMapping("/{barbershopId}/client/{clientId}")
    public ResponseEntity<BarbershopDTO> addClient(
            @PathVariable Integer barbershopId,
            @PathVariable Long clientId) throws DataBaseException {
        return ResponseEntity.ok(barbershopService.addClientToBarbershop(barbershopId, clientId));
    }

    @Operation(summary = "Desvincular cliente de barbearia")
    @DeleteMapping("/{barbershopId}/client/{clientId}")
    public ResponseEntity<BarbershopDTO> removeClient(
            @PathVariable Integer barbershopId,
            @PathVariable Long clientId) throws DataBaseException {
        return ResponseEntity.ok(barbershopService.removeClientFromBarbershop(barbershopId, clientId));
    }

    @Operation(summary = "Vincular barbeiro a barbearia")
    @PostMapping("/{barbershopId}/barber/{barberId}")
    public ResponseEntity<BarbershopDTO> addBarber(
            @PathVariable Integer barbershopId,
            @PathVariable Integer barberId) throws DataBaseException {
        return ResponseEntity.ok(barbershopService.addBarberToBarbershop(barbershopId, barberId));
    }

    @Operation(summary = "Desvincular barbeiro de barbearia")
    @DeleteMapping("/{barbershopId}/barber/{barberId}")
    public ResponseEntity<BarbershopDTO> removeBarber(
            @PathVariable Integer barbershopId,
            @PathVariable Integer barberId) throws DataBaseException {
        return ResponseEntity.ok(barbershopService.removeBarberFromBarbershop(barbershopId, barberId));
    }
}
