
package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.doc.ClientControllerDoc;
import br.edu.ufape.gobarber.dto.client.ClientCreateDTO;
import br.edu.ufape.gobarber.dto.client.ClientDTO;
import br.edu.ufape.gobarber.dto.page.PageDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.exceptions.NotFoundException;
import br.edu.ufape.gobarber.model.Client;
import br.edu.ufape.gobarber.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/client")
@Validated
@RequiredArgsConstructor
public class ClientController implements ClientControllerDoc {
        @Override
        @GetMapping("/top-spenders")
        public ResponseEntity<List<ClientDTO>> getTopSpenders(@RequestParam int limit) {
            // Implementação simples: retornar não implementado
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }

    @Override
    @GetMapping("/inactive-clients")
    public ResponseEntity<List<ClientDTO>> getInactiveClients(@RequestParam int days) {
        // Implementação simples: retornar não implementado
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    @GetMapping("/{id}/loyalty-discount")
    public ResponseEntity<Double> getLoyaltyDiscount(@PathVariable Long id) throws NotFoundException {
        // Implementação simples: retornar não implementado
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    @GetMapping("/by-loyalty-tier/{tier}")
    public ResponseEntity<List<ClientDTO>> getByLoyaltyTier(@PathVariable Client.LoyaltyTier tier) {
        // Implementação simples: retornar não implementado
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    @GetMapping("/birthdays/today")
    public ResponseEntity<List<ClientDTO>> getBirthdayClientsToday() {
        // Implementação simples: retornar não implementado
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    @GetMapping("/birthdays/month")
    public ResponseEntity<List<ClientDTO>> getBirthdayClientsMonth(@RequestParam Integer month) {
        // Implementação simples: retornar não implementado
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    @GetMapping("/clients-for-promotions")
    public ResponseEntity<List<ClientDTO>> getClientsForPromotions() {
        // Implementação simples: retornar não implementado
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    @GetMapping("/total-clients")
    public ResponseEntity<Long> getTotalClients() {
        // Implementação simples: retornar não implementado
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    @GetMapping("/active-clients")
    public ResponseEntity<Long> getActiveClients() {
        // Implementação simples: retornar não implementado
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    @GetMapping("/loyalty-distribution")
    public ResponseEntity<?> getLoyaltyDistribution() {
        // Implementação simples: retornar não implementado
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    @PostMapping("/{id}/preferred-barber/{barberId}")
    public ResponseEntity<ClientDTO> setPreferredBarber(@PathVariable Long id, @PathVariable Long barberId) {
        // Implementação simples: delega para o service (ajuste conforme regra de
        // negócio)
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    private final ClientService clientService;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClientDTO> create(
            @Valid @RequestPart("client") ClientCreateDTO dto,
            @RequestPart(value = "photo", required = false) MultipartFile photo) throws DataBaseException {
        ClientDTO created = clientService.create(dto, photo);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/create-without-photo")
    public ResponseEntity<ClientDTO> createWithoutPhoto(@Valid @RequestBody ClientCreateDTO dto)
            throws DataBaseException {
        ClientDTO created = clientService.create(dto, null);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClientDTO> update(
            @PathVariable Long id,
            @Valid @RequestPart("client") ClientCreateDTO dto,
            @RequestPart(value = "photo", required = false) MultipartFile photo)
            throws NotFoundException, DataBaseException {
        ClientDTO updated = clientService.update(id, dto, photo);
        return ResponseEntity.ok(updated);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws NotFoundException, DataBaseException {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> findById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<PageDTO<ClientDTO>> findAll(Pageable pageable) {
        Page<ClientDTO> page = clientService.findAll(pageable);
        PageDTO<ClientDTO> pageDTO = PageDTO.fromPage(page);
        return ResponseEntity.ok(pageDTO);
    }

    @Override
    @GetMapping("/email/{email}")
    public ResponseEntity<ClientDTO> findByEmail(@PathVariable String email) throws NotFoundException {
        return ResponseEntity.ok(clientService.findByEmail(email));
    }

    @Override
    @GetMapping("/phone/{phone}")
    public ResponseEntity<ClientDTO> findByPhone(@PathVariable String phone) throws NotFoundException {
        return ResponseEntity.ok(clientService.findByPhone(phone));
    }

    @Override
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClientDTO> findByCpf(@PathVariable String cpf) throws NotFoundException {
        return ResponseEntity.ok(clientService.findByCpf(cpf));
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<List<ClientDTO>> search(@RequestParam String name) {
        return ResponseEntity.ok(clientService.searchByName(name));
    }

    @Override
    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long id) throws NotFoundException {
        byte[] photo = clientService.getPhoto(id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(photo);
    }

    @Override
    @PutMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updatePhoto(
            @PathVariable Long id,
            @RequestPart("photo") MultipartFile photo) throws NotFoundException, DataBaseException {
        clientService.updatePhoto(id, photo);
        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping("/{id}/photo")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) throws NotFoundException {
        clientService.deletePhoto(id);
        return ResponseEntity.ok().build();
    }

    // === Programa de Fidelidade ===

    @Override
    @PostMapping("/{id}/loyalty/add")
    public ResponseEntity<ClientDTO> addLoyaltyPoints(
            @PathVariable Long id,
            @RequestParam int points,
            @RequestParam(required = false) String reason) throws NotFoundException {
        return ResponseEntity.ok(clientService.addLoyaltyPoints(id, points, reason));
    }

    @Override
    @PostMapping("/{id}/loyalty/redeem")
    public ResponseEntity<ClientDTO> redeemLoyaltyPoints(
            @PathVariable Long id,
            @RequestParam int points) throws NotFoundException {
        return ResponseEntity.ok(clientService.redeemLoyaltyPoints(id, points));
    }

    @Override
    @PostMapping("/{id}/visit")
    public ResponseEntity<ClientDTO> registerVisit(
            @PathVariable Long id,
            @RequestParam double amount) throws NotFoundException {
        return ResponseEntity.ok(clientService.registerVisit(id, amount));
    }

    @GetMapping("/top-clients")
    public ResponseEntity<List<ClientDTO>> getTopClients(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(clientService.getTopClients(limit));
    }

    @GetMapping("/vip")
    public ResponseEntity<List<ClientDTO>> getVipClients() {
        return ResponseEntity.ok(clientService.getVipClients());
    }

    @GetMapping("/birthdays")
    public ResponseEntity<List<ClientDTO>> getBirthdaysThisMonth() {
        return ResponseEntity.ok(clientService.getClientsWithBirthdayThisMonth());
    }

    @Override
    @GetMapping("/preferred-barber/{barberId}")
    public ResponseEntity<List<ClientDTO>> getClientsByPreferredBarber(@PathVariable Long barberId) {
        List<ClientDTO> clients = clientService.getClientsByPreferredBarber(barberId);
        return ResponseEntity.ok(clients);
    }
}
