package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.doc.PaymentControllerDoc;
import br.edu.ufape.gobarber.dto.page.PageDTO;
import br.edu.ufape.gobarber.dto.payment.PaymentCreateDTO;
import br.edu.ufape.gobarber.dto.payment.PaymentDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.exceptions.NotFoundException;
import br.edu.ufape.gobarber.model.Payment;
import br.edu.ufape.gobarber.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@Validated
@RequiredArgsConstructor
public class PaymentController implements PaymentControllerDoc {

    private final PaymentService paymentService;

    @Override
    @PostMapping
    public ResponseEntity<PaymentDTO> create(@Valid @RequestBody PaymentCreateDTO dto) throws DataBaseException {
        PaymentDTO created = paymentService.createPayment(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> findById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(paymentService.findById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<PageDTO<PaymentDTO>> findAll(Pageable pageable) {
        Page<PaymentDTO> page = paymentService.findAll(pageable);
        PageDTO<PaymentDTO> pageDTO = PageDTO.fromPage(page);
        return ResponseEntity.ok(pageDTO);
    }

    @Override
    @PostMapping("/{id}/confirm")
    public ResponseEntity<PaymentDTO> confirm(
            @PathVariable Long id,
            @RequestParam String transactionId) throws NotFoundException {
        try {
            return ResponseEntity.ok(paymentService.confirmPayment(id, transactionId));
        } catch (DataBaseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @PostMapping("/{id}/cancel")
    public ResponseEntity<PaymentDTO> cancel(@PathVariable Long id) throws NotFoundException {
        try {
            return ResponseEntity.ok(paymentService.cancelPayment(id));
        } catch (DataBaseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentDTO> refund(
            @PathVariable Long id,
            @RequestParam String reason) throws NotFoundException {
        try {
            return ResponseEntity.ok(paymentService.refundPayment(id, reason));
        } catch (DataBaseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @PostMapping("/{id}/partial-refund")
    public ResponseEntity<PaymentDTO> partialRefund(
            @PathVariable Long id,
            @RequestParam double amount,
            @RequestParam String reason) throws NotFoundException {
        try {
            return ResponseEntity.ok(paymentService.partialRefund(id, amount, reason));
        } catch (DataBaseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @GetMapping("/{id}/pix-code")
    public ResponseEntity<String> getPixCode(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(paymentService.getPixCode(id));
    }

    @Override
    @GetMapping("/{id}/pix-qrcode")
    public ResponseEntity<String> getPixQrCode(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(paymentService.getPixQrCode(id));
    }

    @Override
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentDTO>> findByStatus(
            @PathVariable Payment.PaymentStatus status) {
        return ResponseEntity.ok(paymentService.findByStatus(status));
    }

    @Override
    @GetMapping("/method/{method}")
    public ResponseEntity<List<PaymentDTO>> findByMethod(
            @PathVariable Payment.PaymentMethod method) {
        return ResponseEntity.ok(paymentService.findByMethod(method));
    }

    @Override
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PaymentDTO> findByAppointment(@PathVariable Long appointmentId) throws NotFoundException {
        return ResponseEntity.ok(paymentService.findByAppointment(appointmentId));
    }

    @Override
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PaymentDTO>> findByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(paymentService.findByClient(clientId));
    }

    @Override
    @GetMapping("/barber/{barberId}")
    public ResponseEntity<List<PaymentDTO>> findByBarber(@PathVariable Long barberId) {
        return ResponseEntity.ok(paymentService.findByBarber(barberId));
    }

    @Override
    @GetMapping("/period")
    public ResponseEntity<List<PaymentDTO>> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(paymentService.findByDateRange(startDate, endDate));
    }

    // === Relatórios Financeiros ===

    @Override
    @GetMapping("/revenue/total")
    public ResponseEntity<Double> getTotalRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(paymentService.getTotalRevenue(startDate, endDate));
    }

    @Override
    @GetMapping("/revenue/today")
    public ResponseEntity<Double> getTodayRevenue() {
        return ResponseEntity.ok(paymentService.getTodayRevenue());
    }

    @Override
    @GetMapping("/revenue/month")
    public ResponseEntity<Double> getMonthRevenue() {
        return ResponseEntity.ok(paymentService.getMonthRevenue());
    }

    @Override
    @GetMapping("/revenue/daily")
    public ResponseEntity<List<Map<String, Object>>> getDailyRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(paymentService.getDailyRevenue(startDate, endDate));
    }

    @Override
    @GetMapping("/barber/{barberId}/revenue")
    public ResponseEntity<Double> getBarberRevenue(
            @PathVariable Long barberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(paymentService.getBarberRevenue(barberId, startDate, endDate));
    }

    @Override
    @GetMapping("/barber/{barberId}/commission")
    public ResponseEntity<Double> getBarberCommission(
            @PathVariable Long barberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(paymentService.getBarberCommission(barberId, startDate, endDate));
    }

    @Override
    @GetMapping("/average-ticket")
    public ResponseEntity<Double> getAverageTicket(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(paymentService.getAverageTicket(startDate, endDate));
    }

    @Override
    @GetMapping("/revenue/by-method")
    public ResponseEntity<List<Map<String, Object>>> getRevenueByMethod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(paymentService.getRevenueByPaymentMethod(startDate, endDate));
    }

    @Override
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalPayments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(paymentService.countPayments(startDate, endDate));
    }

    @Override
    @GetMapping("/pending")
    public ResponseEntity<List<PaymentDTO>> getPendingPayments() {
        return ResponseEntity.ok(paymentService.findByStatus(Payment.PaymentStatus.PENDING));
    }

    @Override
    @GetMapping("/pending/count")
    public ResponseEntity<Long> getPendingPaymentsCount() {
        return ResponseEntity.ok(paymentService.countPendingPayments());
    }
}
