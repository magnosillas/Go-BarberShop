package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.dashboard.DashboardDTO;
import br.edu.ufape.gobarber.model.Client;
import br.edu.ufape.gobarber.repository.*;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private BarberRepository barberRepository;

    @InjectMocks
    private DashboardService dashboardService;

    private LocalDate startDate;
    private LocalDate endDate;
    private Client client;

    @BeforeEach
    void setUp() {
        startDate = LocalDate.now().minusDays(30);
        endDate = LocalDate.now();
        
        client = new Client();
        client.setIdClient(1L);
        client.setName("João Silva");
        client.setTotalSpent(1000.0);
        client.setTotalVisits(10);
        client.setLoyaltyTier(Client.LoyaltyTier.SILVER);
    }
    
    private void setupBaseMocks() {
        Page<Client> clientPage = new PageImpl<>(Arrays.asList(client));
        when(clientRepository.findTopSpenders(any(Pageable.class))).thenReturn(clientPage);
        when(paymentRepository.countAndSumByPaymentMethod(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(paymentRepository.dailyRevenue(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
    }

    @Test
    void testGetDashboard_Success() {
        // Arrange
        setupBaseMocks();
        when(paymentRepository.sumRevenueByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(10000.0);
        when(paymentRepository.averageTicketByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(150.0);
        when(paymentRepository.countCompletedByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(66L);
        when(paymentRepository.revenueByBarber(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.countByStartTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(100L);
        when(appointmentRepository.countByStartTimeBefore(any(LocalDateTime.class)))
                .thenReturn(80L);
        when(clientRepository.count()).thenReturn(50L);
        when(clientRepository.countNewClients(any(LocalDateTime.class))).thenReturn(10L);
        when(reviewRepository.overallAverageRating()).thenReturn(4.5);
        when(reviewRepository.countTotal()).thenReturn(30L);
        when(reviewRepository.countWouldRecommend()).thenReturn(28L);
        when(barberRepository.count()).thenReturn(5L);

        // Act
        DashboardDTO result = dashboardService.getDashboard(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(10000.0, result.getTotalRevenue());
        assertEquals(150.0, result.getAverageTicket());
        assertEquals(66L, result.getTotalTransactions());
        assertEquals(100L, result.getTotalAppointments());
        assertEquals(50L, result.getTotalClients());
        assertEquals(10L, result.getNewClients());
        assertEquals(4.5, result.getAverageRating());
    }

    @Test
    void testGetDashboard_WithNullRevenue() {
        // Arrange
        setupBaseMocks();
        when(paymentRepository.sumRevenueByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(null);
        when(paymentRepository.averageTicketByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(null);
        when(paymentRepository.countCompletedByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(0L);
        when(paymentRepository.revenueByBarber(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.countByStartTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(null);
        when(appointmentRepository.countByStartTimeBefore(any(LocalDateTime.class)))
                .thenReturn(null);
        when(clientRepository.count()).thenReturn(0L);
        when(clientRepository.countNewClients(any(LocalDateTime.class))).thenReturn(0L);
        when(reviewRepository.overallAverageRating()).thenReturn(null);
        when(reviewRepository.countTotal()).thenReturn(0L);
        when(reviewRepository.countWouldRecommend()).thenReturn(0L);
        when(barberRepository.count()).thenReturn(0L);

        // Act
        DashboardDTO result = dashboardService.getDashboard(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(0.0, result.getTotalRevenue());
        assertEquals(0.0, result.getAverageTicket());
        assertEquals(0L, result.getTotalAppointments());
    }

    @Test
    void testGetDashboard_RevenueGrowthCalculation() {
        // Arrange - período atual com 10000, período anterior com 8000 = 25% crescimento
        setupBaseMocks();
        when(paymentRepository.sumRevenueByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(10000.0, 8000.0); // Primeira chamada = atual, segunda = anterior
        when(paymentRepository.averageTicketByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(150.0);
        when(paymentRepository.countCompletedByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(66L);
        when(paymentRepository.revenueByBarber(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.countByStartTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(100L);
        when(appointmentRepository.countByStartTimeBefore(any(LocalDateTime.class)))
                .thenReturn(80L);
        when(clientRepository.count()).thenReturn(50L);
        when(clientRepository.countNewClients(any(LocalDateTime.class))).thenReturn(10L);
        when(reviewRepository.overallAverageRating()).thenReturn(4.5);
        when(reviewRepository.countTotal()).thenReturn(30L);
        when(reviewRepository.countWouldRecommend()).thenReturn(28L);
        when(barberRepository.count()).thenReturn(5L);

        // Act
        DashboardDTO result = dashboardService.getDashboard(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(startDate, result.getStartDate());
        assertEquals(endDate, result.getEndDate());
    }

    @Test
    void testGetDashboard_ClientRetentionRate() {
        // Arrange
        setupBaseMocks();
        when(paymentRepository.sumRevenueByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(10000.0);
        when(paymentRepository.averageTicketByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(150.0);
        when(paymentRepository.countCompletedByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(66L);
        when(paymentRepository.revenueByBarber(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.countByStartTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(100L);
        when(appointmentRepository.countByStartTimeBefore(any(LocalDateTime.class)))
                .thenReturn(80L);
        when(clientRepository.count()).thenReturn(100L);
        when(clientRepository.countNewClients(any(LocalDateTime.class))).thenReturn(20L);
        when(reviewRepository.overallAverageRating()).thenReturn(4.5);
        when(reviewRepository.countTotal()).thenReturn(30L);
        when(reviewRepository.countWouldRecommend()).thenReturn(28L);
        when(barberRepository.count()).thenReturn(5L);

        // Act
        DashboardDTO result = dashboardService.getDashboard(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getTotalClients());
        assertEquals(20L, result.getNewClients());
        assertEquals(80L, result.getReturningClients()); // 100 - 20
        assertEquals(80.0, result.getClientRetentionRate()); // 80/100 * 100
    }

    @Test
    void testGetDashboard_RecommendationRate() {
        // Arrange
        setupBaseMocks();
        when(paymentRepository.sumRevenueByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(10000.0);
        when(paymentRepository.averageTicketByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(150.0);
        when(paymentRepository.countCompletedByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(66L);
        when(paymentRepository.revenueByBarber(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.countByStartTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(100L);
        when(appointmentRepository.countByStartTimeBefore(any(LocalDateTime.class)))
                .thenReturn(80L);
        when(clientRepository.count()).thenReturn(50L);
        when(clientRepository.countNewClients(any(LocalDateTime.class))).thenReturn(10L);
        when(reviewRepository.overallAverageRating()).thenReturn(4.5);
        when(reviewRepository.countTotal()).thenReturn(100L);
        when(reviewRepository.countWouldRecommend()).thenReturn(90L);
        when(barberRepository.count()).thenReturn(5L);

        // Act
        DashboardDTO result = dashboardService.getDashboard(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getTotalReviews());
        assertEquals(90.0, result.getRecommendationRate()); // 90/100 * 100
    }

    @Test
    void testGetDashboard_WithZeroClients() {
        // Arrange
        setupBaseMocks();
        when(paymentRepository.sumRevenueByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(0.0);
        when(paymentRepository.averageTicketByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(0.0);
        when(paymentRepository.countCompletedByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(0L);
        when(paymentRepository.revenueByBarber(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.countByStartTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(0L);
        when(appointmentRepository.countByStartTimeBefore(any(LocalDateTime.class)))
                .thenReturn(0L);
        when(clientRepository.count()).thenReturn(0L);
        when(clientRepository.countNewClients(any(LocalDateTime.class))).thenReturn(0L);
        when(reviewRepository.overallAverageRating()).thenReturn(0.0);
        when(reviewRepository.countTotal()).thenReturn(0L);
        when(reviewRepository.countWouldRecommend()).thenReturn(0L);
        when(barberRepository.count()).thenReturn(0L);

        // Act
        DashboardDTO result = dashboardService.getDashboard(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(0L, result.getTotalClients());
        assertEquals(0.0, result.getClientRetentionRate());
    }

    @Test
    void testGetDashboard_OccupancyRateCalculation() {
        // Arrange
        setupBaseMocks();
        when(paymentRepository.sumRevenueByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(10000.0);
        when(paymentRepository.averageTicketByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(150.0);
        when(paymentRepository.countCompletedByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(66L);
        when(paymentRepository.revenueByBarber(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(appointmentRepository.countByStartTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(100L);
        when(appointmentRepository.countByStartTimeBefore(any(LocalDateTime.class)))
                .thenReturn(80L);
        when(clientRepository.count()).thenReturn(50L);
        when(clientRepository.countNewClients(any(LocalDateTime.class))).thenReturn(10L);
        when(reviewRepository.overallAverageRating()).thenReturn(4.5);
        when(reviewRepository.countTotal()).thenReturn(30L);
        when(reviewRepository.countWouldRecommend()).thenReturn(28L);
        when(barberRepository.count()).thenReturn(5L);

        // Act
        DashboardDTO result = dashboardService.getDashboard(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertTrue(result.getOccupancyRate() >= 0);
    }
}
