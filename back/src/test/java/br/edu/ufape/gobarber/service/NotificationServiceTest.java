package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.model.*;
import br.edu.ufape.gobarber.repository.ClientRepository;
import br.edu.ufape.gobarber.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationService notificationService;

    private Client client;
    private Barber barber;
    private Appointment appointment;
    private Notification notification;
    private Sale sale;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setIdClient(1L);
        client.setName("João Silva");
        client.setEmail("aderamos108@gmail.com");
        client.setReceivePromotions(true);
        client.setLoyaltyPoints(100);

        barber = new Barber();
        barber.setIdBarber(1);
        barber.setName("Carlos Barbeiro");

        appointment = new Appointment();
        appointment.setId(1);
        appointment.setBarber(barber);
        appointment.setStartTime(LocalDateTime.now().plusDays(1));
        appointment.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));
        appointment.setTotalPrice(50.0);

        notification = Notification.builder()
                .idNotification(1L)
                .client(client)
                .type(Notification.NotificationType.APPOINTMENT_CONFIRMATION)
                .title("Agendamento Confirmado!")
                .message("Seu agendamento foi confirmado")
                .status(Notification.NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        sale = new Sale();
        sale.setIdSale(1);
        sale.setName("Promoção de Fim de Ano");
        sale.setCoupon("FIMDEANO2025");
        sale.setEndDate(LocalDate.now().plusDays(30));
        sale.setTotalPrice(50.0);
    }

    @Test
    void testCreateAppointmentConfirmation_Success() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.createAppointmentConfirmation(appointment, client);

        assertNotNull(result);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testCreateAppointmentReminder_Success() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.createAppointmentReminder(appointment, client);

        assertNotNull(result);
        verify(notificationRepository, times(2)).save(any(Notification.class));
    }

    @Test
    void testCreateAppointmentCancellation_Success() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.createAppointmentCancellation(appointment, client);

        assertNotNull(result);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testCreatePromotionNotifications_Success() {
        List<Client> clients = Arrays.asList(client);
        when(clientRepository.findByReceivePromotionsTrue()).thenReturn(clients);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        notificationService.createPromotionNotifications(sale);

        verify(clientRepository, times(1)).findByReceivePromotionsTrue();
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testCreateLoyaltyPointsNotification_Success() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.createLoyaltyPointsNotification(client, 50, 150);

        assertNotNull(result);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testCreateBirthdayNotification_Success() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.createBirthdayNotification(client);

        assertNotNull(result);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testCreateWelcomeNotification_Success() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.createWelcomeNotification(client);

        assertNotNull(result);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testCreateReviewRequestNotification_Success() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.createReviewRequestNotification(appointment, client);

        assertNotNull(result);
        verify(notificationRepository, atLeast(1)).save(any(Notification.class));
    }

    @Test
    void testGetClientNotifications_Success() {
        Page<Notification> page = new PageImpl<>(Arrays.asList(notification));
        when(notificationRepository.findByClientIdClientOrderByCreatedAtDesc(eq(1L), any(Pageable.class))).thenReturn(page);

        Page<Notification> result = notificationService.getClientNotifications(1L, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGetUnreadNotifications_Success() {
        List<Notification> notifications = Arrays.asList(notification);
        when(notificationRepository.findUnreadByClient(1L)).thenReturn(notifications);

        List<Notification> result = notificationService.getUnreadNotifications(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testMarkAsRead_Success() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        notificationService.markAsRead(1L);

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testMarkAllAsRead_Success() {
        List<Notification> notifications = Arrays.asList(notification);
        when(notificationRepository.findUnreadByClient(1L)).thenReturn(notifications);
        when(notificationRepository.saveAll(any())).thenReturn(notifications);

        notificationService.markAllAsRead(1L);

        verify(notificationRepository, times(1)).saveAll(any());
    }

    @Test
    void testDeleteNotification_Success() {
        doNothing().when(notificationRepository).deleteById(1L);

        notificationService.deleteNotification(1L);

        verify(notificationRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCountUnreadNotifications_Success() {
        when(notificationRepository.countUnreadByClient(1L)).thenReturn(5L);

        Long count = notificationService.getUnreadCount(1L);

        assertEquals(5L, count);
    }

    @Test
    void testNotificationType_AllValues() {
        Notification.NotificationType[] types = Notification.NotificationType.values();
        assertTrue(types.length >= 5);
        assertNotNull(Notification.NotificationType.APPOINTMENT_CONFIRMATION);
        assertNotNull(Notification.NotificationType.APPOINTMENT_REMINDER);
        assertNotNull(Notification.NotificationType.PROMOTION);
    }

    @Test
    void testNotificationStatus_AllValues() {
        Notification.NotificationStatus[] statuses = Notification.NotificationStatus.values();
        assertTrue(statuses.length >= 3);
        assertNotNull(Notification.NotificationStatus.PENDING);
        assertNotNull(Notification.NotificationStatus.SENT);
    }
}
