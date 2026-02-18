package br.edu.ufape.gobarber.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DTO para dados do Dashboard gerencial.
 */
@Data
@lombok.Getter
@lombok.Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {

    private LocalDate startDate;
    private LocalDate endDate;

    // Resumo Financeiro
    private Double totalRevenue;
    private Double totalCommissions;
    private Double averageTicket;
    private Long totalTransactions;
    private Double revenueGrowth; // % comparado ao período anterior

    // Agendamentos
    private Long totalAppointments;
    private Long completedAppointments;
    private Long cancelledAppointments;
    private Double cancellationRate;
    private Double occupancyRate;

    // Clientes
    private Long totalClients;
    private Long newClients;
    private Long returningClients;
    private Double clientRetentionRate;

    // Avaliações
    private Double averageRating;
    private Long totalReviews;
    private Double recommendationRate;

    // Top Performers
    private List<BarberPerformance> topBarbers;
    private List<ServicePerformance> topServices;
    private List<ClientPerformance> topClients;

    // Distribuições
    private Map<String, Double> revenueByPaymentMethod;
    private Map<String, Long> appointmentsByDayOfWeek;
    private Map<String, Long> appointmentsByHour;

    // Série temporal
    private List<DailyRevenue> dailyRevenue;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BarberPerformance {
        private Integer barberId;
        private String barberName;
        private Double revenue;
        private Long appointments;
        private Double averageRating;
        private Double commission;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ServicePerformance {
        private Integer serviceId;
        private String serviceName;
        private Long timesBooked;
        private Double revenue;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ClientPerformance {
        private Long clientId;
        private String clientName;
        private Double totalSpent;
        private Integer visits;
        private String loyaltyTier;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DailyRevenue {
        private LocalDate date;
        private Double revenue;
        private Long transactions;
    }
}
