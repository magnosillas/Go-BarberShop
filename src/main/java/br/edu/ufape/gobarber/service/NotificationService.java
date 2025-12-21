package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.model.Appointment;
import br.edu.ufape.gobarber.model.Client;
import br.edu.ufape.gobarber.model.Notification;
import br.edu.ufape.gobarber.model.Sale;
import br.edu.ufape.gobarber.repository.ClientRepository;
import br.edu.ufape.gobarber.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ClientRepository clientRepository;
    private final EmailService emailService;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    // === Criação de Notificações ===

    @Transactional
    public Notification createAppointmentConfirmation(Appointment appointment, Client client) {
        String title = "Agendamento Confirmado!";
        String message = String.format(
                "Olá %s! Seu agendamento foi confirmado para %s às %s com %s. " +
                "Valor total: R$ %.2f. Caso precise remarcar, entre em contato conosco.",
                client.getName(),
                appointment.getStartTime().format(DATE_FORMAT),
                appointment.getStartTime().format(TIME_FORMAT),
                appointment.getBarber().getName(),
                appointment.getTotalPrice()
        );

        return createNotification(
                client,
                null,
                appointment,
                Notification.NotificationType.APPOINTMENT_CONFIRMATION,
                title,
                message
        );
    }

    @Transactional
    public Notification createAppointmentReminder(Appointment appointment, Client client) {
        String title = "Lembrete de Agendamento";
        String message = String.format(
                "Olá %s! Lembramos que você tem um agendamento amanhã às %s com %s. " +
                "Estamos te esperando!",
                client.getName(),
                appointment.getStartTime().format(TIME_FORMAT),
                appointment.getBarber().getName()
        );

        Notification notification = createNotification(
                client,
                null,
                appointment,
                Notification.NotificationType.APPOINTMENT_REMINDER,
                title,
                message
        );
        
        // Agendar para envio no dia anterior
        notification.setScheduledFor(appointment.getStartTime().minusDays(1).withHour(10).withMinute(0));
        notification.setStatus(Notification.NotificationStatus.SCHEDULED);
        
        return notificationRepository.save(notification);
    }

    @Transactional
    public Notification createAppointmentCancellation(Appointment appointment, Client client) {
        String title = "Agendamento Cancelado";
        String message = String.format(
                "Olá %s! Seu agendamento de %s às %s foi cancelado. " +
                "Se desejar reagendar, acesse nosso aplicativo ou entre em contato.",
                client.getName(),
                appointment.getStartTime().format(DATE_FORMAT),
                appointment.getStartTime().format(TIME_FORMAT)
        );

        return createNotification(
                client,
                null,
                appointment,
                Notification.NotificationType.APPOINTMENT_CANCELLED,
                title,
                message
        );
    }

    @Transactional
    public void createPromotionNotifications(Sale sale) {
        List<Client> clients = clientRepository.findByReceivePromotionsTrue();
        
        String title = "Nova Promoção! " + sale.getName();
        String message = String.format(
                "Aproveite nossa promoção %s! Use o cupom %s e ganhe desconto. " +
                "Válido até %s. Corra e agende seu horário!",
                sale.getName(),
                sale.getCoupon(),
                sale.getEndDate().format(DATE_FORMAT)
        );

        for (Client client : clients) {
            createNotification(
                    client,
                    null,
                    null,
                    Notification.NotificationType.PROMOTION,
                    title,
                    message
            );
        }
    }

    @Transactional
    public Notification createLoyaltyPointsNotification(Client client, int pointsEarned, int totalPoints) {
        String title = "Pontos de Fidelidade!";
        String message = String.format(
                "Parabéns %s! Você ganhou %d pontos de fidelidade. " +
                "Seu total agora é %d pontos. Continue acumulando para ganhar descontos exclusivos!",
                client.getName(),
                pointsEarned,
                totalPoints
        );

        return createNotification(
                client,
                null,
                null,
                Notification.NotificationType.LOYALTY_POINTS,
                title,
                message
        );
    }

    @Transactional
    public Notification createBirthdayNotification(Client client) {
        String title = "Feliz Aniversário! 🎂";
        String message = String.format(
                "Parabéns %s! Desejamos um excelente dia! " +
                "Para comemorar, você ganhou 50 pontos de fidelidade extras. " +
                "Aproveite para agendar um serviço especial!",
                client.getName()
        );

        return createNotification(
                client,
                null,
                null,
                Notification.NotificationType.BIRTHDAY,
                title,
                message
        );
    }

    @Transactional
    public Notification createWelcomeNotification(Client client) {
        String title = "Bem-vindo à GoBarber!";
        String message = String.format(
                "Olá %s! Seja bem-vindo à GoBarber! " +
                "Você já começou com 10 pontos de fidelidade. " +
                "Agende seu primeiro serviço e comece a acumular mais pontos!",
                client.getName()
        );

        return createNotification(
                client,
                null,
                null,
                Notification.NotificationType.WELCOME,
                title,
                message
        );
    }

    @Transactional
    public Notification createReviewRequestNotification(Appointment appointment, Client client) {
        String title = "Como foi seu atendimento?";
        String message = String.format(
                "Olá %s! Como foi seu atendimento com %s? " +
                "Sua opinião é muito importante para nós. " +
                "Avalie e ganhe 5 pontos de fidelidade!",
                client.getName(),
                appointment.getBarber().getName()
        );

        Notification notification = createNotification(
                client,
                null,
                appointment,
                Notification.NotificationType.REVIEW_REQUEST,
                title,
                message
        );
        
        // Agendar para 1 hora após o término do atendimento
        notification.setScheduledFor(appointment.getEndTime().plusHours(1));
        notification.setStatus(Notification.NotificationStatus.SCHEDULED);
        
        return notificationRepository.save(notification);
    }

    // === Envio de Notificações ===

    @Async
    @Transactional
    public void sendNotification(Notification notification) {
        try {
            switch (notification.getChannel()) {
                case EMAIL:
                    sendEmailNotification(notification);
                    break;
                case SMS:
                    sendSmsNotification(notification);
                    break;
                case WHATSAPP:
                    sendWhatsAppNotification(notification);
                    break;
                case PUSH:
                    sendPushNotification(notification);
                    break;
            }
        } catch (Exception e) {
            log.error("Erro ao enviar notificação {}: {}", notification.getIdNotification(), e.getMessage());
            notification.markAsFailed(e.getMessage());
            notificationRepository.save(notification);
        }
    }

    private void sendEmailNotification(Notification notification) {
        try {
            emailService.sendEmail(
                    notification.getRecipientEmail(),
                    notification.getTitle(),
                    notification.getMessage()
            );
            notification.markAsSent("EMAIL-" + System.currentTimeMillis());
            notificationRepository.save(notification);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar email: " + e.getMessage());
        }
    }

    private void sendSmsNotification(Notification notification) {
        // Integração com provedor de SMS (Twilio, AWS SNS, etc.)
        log.info("SMS para {}: {}", notification.getRecipientPhone(), notification.getMessage());
        notification.markAsSent("SMS-" + System.currentTimeMillis());
        notificationRepository.save(notification);
    }

    private void sendWhatsAppNotification(Notification notification) {
        // Integração com WhatsApp Business API
        log.info("WhatsApp para {}: {}", notification.getRecipientPhone(), notification.getMessage());
        notification.markAsSent("WA-" + System.currentTimeMillis());
        notificationRepository.save(notification);
    }

    private void sendPushNotification(Notification notification) {
        // Integração com Firebase Cloud Messaging ou similar
        log.info("Push notification: {}", notification.getTitle());
        notification.markAsSent("PUSH-" + System.currentTimeMillis());
        notificationRepository.save(notification);
    }

    // === Processamento Agendado ===

    @Scheduled(fixedRate = 60000) // A cada minuto
    @Transactional
    public void processScheduledNotifications() {
        List<Notification> pendingNotifications = notificationRepository
                .findPendingToSend(LocalDateTime.now());
        
        for (Notification notification : pendingNotifications) {
            sendNotification(notification);
        }
    }

    @Scheduled(cron = "0 0 10 * * *") // Todo dia às 10h
    @Transactional
    public void sendBirthdayNotifications() {
        List<Client> birthdayClients = clientRepository.findBirthdayClients(
                LocalDateTime.now().getMonthValue(),
                LocalDateTime.now().getDayOfMonth()
        );
        
        for (Client client : birthdayClients) {
            Notification notification = createBirthdayNotification(client);
            sendNotification(notification);
            
            // Adiciona pontos de aniversário
            client.addLoyaltyPoints(50);
            clientRepository.save(client);
        }
    }

    @Scheduled(fixedRate = 300000) // A cada 5 minutos
    @Transactional
    public void retryFailedNotifications() {
        List<Notification> failedNotifications = notificationRepository.findFailedForRetry(3);
        
        for (Notification notification : failedNotifications) {
            sendNotification(notification);
        }
    }

    // === Consultas ===

    public List<Notification> getUnreadNotifications(Long clientId) {
        return notificationRepository.findUnreadByClient(clientId);
    }

    public Long getUnreadCount(Long clientId) {
        return notificationRepository.countUnreadByClient(clientId);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
        notification.markAsRead();
        notificationRepository.save(notification);
    }

    // === Helper ===

    private Notification createNotification(
            Client client,
            br.edu.ufape.gobarber.model.Barber barber,
            Appointment appointment,
            Notification.NotificationType type,
            String title,
            String message) {
        
        Notification.NotificationChannel channel = client.getPreferredContactMethod() != null ?
                mapContactMethodToChannel(client.getPreferredContactMethod()) :
                Notification.NotificationChannel.EMAIL;

        Notification notification = Notification.builder()
                .client(client)
                .barber(barber)
                .appointment(appointment)
                .type(type)
                .channel(channel)
                .title(title)
                .message(message)
                .status(Notification.NotificationStatus.PENDING)
                .recipientEmail(client.getEmail())
                .recipientPhone(client.getPhone())
                .build();

        return notificationRepository.save(notification);
    }

    private Notification.NotificationChannel mapContactMethodToChannel(Client.ContactMethod method) {
        return switch (method) {
            case EMAIL -> Notification.NotificationChannel.EMAIL;
            case SMS -> Notification.NotificationChannel.SMS;
            case WHATSAPP -> Notification.NotificationChannel.WHATSAPP;
            case PHONE -> Notification.NotificationChannel.SMS;
        };
    }

    // === Métodos para Controller ===

    public Page<Notification> getClientNotifications(Long clientId, Pageable pageable) {
        return notificationRepository.findByClientIdClientOrderByCreatedAtDesc(clientId, pageable);
    }

    public void markAllAsRead(Long clientId) {
        List<Notification> unread = notificationRepository.findUnreadByClient(clientId);
        for (Notification n : unread) {
            n.markAsRead();
        }
        notificationRepository.saveAll(unread);
    }

    public List<Notification> getPendingNotifications() {
        return notificationRepository.findByStatus(Notification.NotificationStatus.PENDING);
    }

    public List<Notification> getFailedNotifications() {
        return notificationRepository.findByStatus(Notification.NotificationStatus.FAILED);
    }

    public void resendNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
        notification.setStatus(Notification.NotificationStatus.PENDING);
        notification.setRetryCount(0);
        notificationRepository.save(notification);
        sendNotification(notification);
    }

    public Map<String, Object> getNotificationStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("pending", notificationRepository.countByStatus(Notification.NotificationStatus.PENDING));
        stats.put("sent", notificationRepository.countByStatus(Notification.NotificationStatus.SENT));
        stats.put("failed", notificationRepository.countByStatus(Notification.NotificationStatus.FAILED));
        stats.put("read", notificationRepository.countByStatus(Notification.NotificationStatus.READ));
        return stats;
    }

    public void sendTestNotification(Long clientId, String title, String message) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        Notification notification = createNotification(
            client, null, null,
            Notification.NotificationType.PROMOTION,
            title, message
        );
        sendNotification(notification);
    }

    public List<Notification> getRecentNotifications(Long clientId, int limit) {
        return notificationRepository.findByClientIdClientOrderByCreatedAtDesc(clientId, PageRequest.of(0, limit))
                .getContent();
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    public long deleteOldNotifications(Long clientId, int daysOld) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(daysOld);
        List<Notification> old = notificationRepository.findByClientIdClientAndCreatedAtBefore(clientId, cutoff);
        long count = old.size();
        notificationRepository.deleteAll(old);
        return count;
    }
}
