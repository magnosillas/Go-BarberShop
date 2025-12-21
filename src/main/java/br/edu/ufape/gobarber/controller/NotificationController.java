package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.doc.NotificationControllerDoc;
import br.edu.ufape.gobarber.model.Notification;
import br.edu.ufape.gobarber.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController implements NotificationControllerDoc {

    private final NotificationService notificationService;

    @Override
    @GetMapping("/client/{clientId}")
    public ResponseEntity<Page<Notification>> getClientNotifications(
            @PathVariable Long clientId, Pageable pageable) {
        return ResponseEntity.ok(notificationService.getClientNotifications(clientId, pageable));
    }

    @Override
    @GetMapping("/client/{clientId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long clientId) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(clientId));
    }

    @Override
    @GetMapping("/client/{clientId}/unread/count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long clientId) {
        return ResponseEntity.ok(notificationService.getUnreadCount(clientId));
    }

    @Override
    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/client/{clientId}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long clientId) {
        notificationService.markAllAsRead(clientId);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/pending")
    public ResponseEntity<List<Notification>> getPendingNotifications() {
        return ResponseEntity.ok(notificationService.getPendingNotifications());
    }

    @Override
    @GetMapping("/failed")
    public ResponseEntity<List<Notification>> getFailedNotifications() {
        return ResponseEntity.ok(notificationService.getFailedNotifications());
    }

    @Override
    @PostMapping("/{id}/resend")
    public ResponseEntity<Void> resendNotification(@PathVariable Long id) {
        notificationService.resendNotification(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(notificationService.getNotificationStats());
    }

    // === Endpoints Adicionais ===

    @PostMapping("/send-test")
    public ResponseEntity<Void> sendTestNotification(
            @RequestParam Long clientId,
            @RequestParam String title,
            @RequestParam String message) {
        notificationService.sendTestNotification(clientId, title, message);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/client/{clientId}/recent")
    public ResponseEntity<List<Notification>> getRecentNotifications(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(notificationService.getRecentNotifications(clientId, limit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/client/{clientId}/old")
    public ResponseEntity<Long> deleteOldNotifications(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "30") int daysOld) {
        long deleted = notificationService.deleteOldNotifications(clientId, daysOld);
        return ResponseEntity.ok(deleted);
    }
}
