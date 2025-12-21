package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.dashboard.DashboardDTO;
import br.edu.ufape.gobarber.model.Client;
import br.edu.ufape.gobarber.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final ClientRepository clientRepository;
    private final ReviewRepository reviewRepository;
    private final BarberRepository barberRepository;

    public DashboardDTO getDashboard(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        // Período anterior para comparação
        long daysDiff = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        LocalDateTime prevStartDateTime = startDate.minusDays(daysDiff + 1).atStartOfDay();
        LocalDateTime prevEndDateTime = startDate.minusDays(1).atTime(LocalTime.MAX);

        DashboardDTO dashboard = new DashboardDTO();
        dashboard.setStartDate(startDate);
        dashboard.setEndDate(endDate);

        // === Financeiro ===
        Double revenue = paymentRepository.sumRevenueByDateRange(startDateTime, endDateTime);
        dashboard.setTotalRevenue(revenue != null ? revenue : 0.0);

        Double prevRevenue = paymentRepository.sumRevenueByDateRange(prevStartDateTime, prevEndDateTime);
        if (prevRevenue != null && prevRevenue > 0) {
            dashboard.setRevenueGrowth(((revenue - prevRevenue) / prevRevenue) * 100);
        } else {
            dashboard.setRevenueGrowth(0.0);
        }

        Double avgTicket = paymentRepository.averageTicketByDateRange(startDateTime, endDateTime);
        dashboard.setAverageTicket(avgTicket != null ? avgTicket : 0.0);

        Long transactions = paymentRepository.countCompletedByDateRange(startDateTime, endDateTime);
        dashboard.setTotalTransactions(transactions);

        // Comissões
        List<Object[]> barberRevenue = paymentRepository.revenueByBarber(startDateTime, endDateTime);
        double totalCommissions = barberRevenue.stream()
                .mapToDouble(row -> row[3] != null ? ((Number) row[3]).doubleValue() : 0.0)
                .sum();
        dashboard.setTotalCommissions(totalCommissions);

        // === Agendamentos ===
        Long totalAppointments = appointmentRepository.countByStartTimeBetween(startDateTime, endDateTime);
        dashboard.setTotalAppointments(totalAppointments != null ? totalAppointments : 0L);

        // Assumindo que agendamentos passados são concluídos
        Long completedAppointments = appointmentRepository.countByStartTimeBefore(LocalDateTime.now());
        dashboard.setCompletedAppointments(completedAppointments != null ? completedAppointments : 0L);

        // Taxa de cancelamento (simulada - precisaria de campo status)
        dashboard.setCancelledAppointments(0L);
        dashboard.setCancellationRate(0.0);

        // Taxa de ocupação (simplificada)
        dashboard.setOccupancyRate(calculateOccupancyRate(startDateTime, endDateTime));

        // === Clientes ===
        Long totalClients = clientRepository.count();
        dashboard.setTotalClients(totalClients);

        Long newClients = clientRepository.countNewClients(startDateTime);
        dashboard.setNewClients(newClients);

        dashboard.setReturningClients(totalClients - newClients);
        
        if (totalClients > 0) {
            dashboard.setClientRetentionRate((dashboard.getReturningClients().doubleValue() / totalClients) * 100);
        } else {
            dashboard.setClientRetentionRate(0.0);
        }

        // === Avaliações ===
        Double avgRating = reviewRepository.overallAverageRating();
        dashboard.setAverageRating(avgRating != null ? avgRating : 0.0);

        Long totalReviews = reviewRepository.countTotal();
        dashboard.setTotalReviews(totalReviews);

        Long recommended = reviewRepository.countWouldRecommend();
        if (totalReviews > 0) {
            dashboard.setRecommendationRate((recommended.doubleValue() / totalReviews) * 100);
        } else {
            dashboard.setRecommendationRate(0.0);
        }

        // === Top Performers ===
        dashboard.setTopBarbers(getTopBarbers(startDateTime, endDateTime));
        dashboard.setTopServices(getTopServices(startDateTime, endDateTime));
        dashboard.setTopClients(getTopClients());

        // === Distribuições ===
        dashboard.setRevenueByPaymentMethod(getRevenueByPaymentMethod(startDateTime, endDateTime));
        dashboard.setAppointmentsByDayOfWeek(getAppointmentsByDayOfWeek(startDateTime, endDateTime));
        dashboard.setAppointmentsByHour(getAppointmentsByHour(startDateTime, endDateTime));

        // === Série Temporal ===
        dashboard.setDailyRevenue(getDailyRevenue(startDateTime, endDateTime));

        return dashboard;
    }

    private Double calculateOccupancyRate(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // Cálculo simplificado: agendamentos / (barbeiros * dias * slots disponíveis)
        long barbers = barberRepository.count();
        if (barbers == 0) return 0.0;

        long days = java.time.temporal.ChronoUnit.DAYS.between(startDateTime.toLocalDate(), endDateTime.toLocalDate()) + 1;
        long slotsPerDay = 8; // Assumindo 8 slots de 1 hora por dia

        Long appointments = appointmentRepository.countByStartTimeBetween(startDateTime, endDateTime);
        if (appointments == null) appointments = 0L;

        long totalSlots = barbers * days * slotsPerDay;
        return (appointments.doubleValue() / totalSlots) * 100;
    }

    private List<DashboardDTO.BarberPerformance> getTopBarbers(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Object[]> data = paymentRepository.revenueByBarber(startDateTime, endDateTime);
        
        return data.stream()
                .limit(5)
                .map(row -> {
                    Integer barberId = (Integer) row[0];
                    Double avgRating = reviewRepository.averageRatingByBarber(barberId);
                    
                    return DashboardDTO.BarberPerformance.builder()
                            .barberId(barberId)
                            .barberName((String) row[1])
                            .revenue(row[2] != null ? ((Number) row[2]).doubleValue() : 0.0)
                            .commission(row[3] != null ? ((Number) row[3]).doubleValue() : 0.0)
                            .averageRating(avgRating != null ? avgRating : 0.0)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<DashboardDTO.ServicePerformance> getTopServices(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // Implementação simplificada - retorna lista vazia
        // Em produção, criar query específica para serviços mais utilizados
        return new ArrayList<>();
    }

    private List<DashboardDTO.ClientPerformance> getTopClients() {
        return clientRepository.findTopSpenders(PageRequest.of(0, 5))
                .map(client -> DashboardDTO.ClientPerformance.builder()
                        .clientId(client.getIdClient())
                        .clientName(client.getName())
                        .totalSpent(client.getTotalSpent())
                        .visits(client.getTotalVisits())
                        .loyaltyTier(client.getLoyaltyTier().name())
                        .build())
                .getContent();
    }

    private Map<String, Double> getRevenueByPaymentMethod(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Object[]> data = paymentRepository.countAndSumByPaymentMethod(startDateTime, endDateTime);
        Map<String, Double> result = new HashMap<>();
        
        for (Object[] row : data) {
            String method = row[0].toString();
            Double amount = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;
            result.put(method, amount);
        }
        
        return result;
    }

    private Map<String, Long> getAppointmentsByDayOfWeek(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // Implementação simplificada - retorna mapa com dias da semana
        Map<String, Long> result = new LinkedHashMap<>();
        String[] days = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo"};
        for (String day : days) {
            result.put(day, 0L);
        }
        return result;
    }

    private Map<String, Long> getAppointmentsByHour(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // Implementação simplificada - retorna mapa com horários
        Map<String, Long> result = new LinkedHashMap<>();
        for (int i = 8; i <= 20; i++) {
            result.put(String.format("%02d:00", i), 0L);
        }
        return result;
    }

    private List<DashboardDTO.DailyRevenue> getDailyRevenue(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Object[]> data = paymentRepository.dailyRevenue(startDateTime, endDateTime);
        
        return data.stream()
                .map(row -> DashboardDTO.DailyRevenue.builder()
                        .date(((java.sql.Date) row[0]).toLocalDate())
                        .revenue(row[1] != null ? ((Number) row[1]).doubleValue() : 0.0)
                        .build())
                .collect(Collectors.toList());
    }

    // === Relatórios Específicos ===

    public Map<String, Object> getFinancialReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        Map<String, Object> report = new HashMap<>();
        
        report.put("totalRevenue", paymentRepository.sumRevenueByDateRange(startDateTime, endDateTime));
        report.put("averageTicket", paymentRepository.averageTicketByDateRange(startDateTime, endDateTime));
        report.put("totalTransactions", paymentRepository.countCompletedByDateRange(startDateTime, endDateTime));
        report.put("revenueByPaymentMethod", paymentRepository.countAndSumByPaymentMethod(startDateTime, endDateTime));
        report.put("revenueByBarber", paymentRepository.revenueByBarber(startDateTime, endDateTime));
        report.put("dailyRevenue", paymentRepository.dailyRevenue(startDateTime, endDateTime));
        
        return report;
    }

    public Map<String, Object> getClientReport() {
        Map<String, Object> report = new HashMap<>();
        
        report.put("totalClients", clientRepository.count());
        report.put("newClientsLast30Days", clientRepository.countNewClients(LocalDateTime.now().minusDays(30)));
        report.put("clientsByLoyaltyTier", clientRepository.countByLoyaltyTier());
        report.put("topSpenders", clientRepository.findTopSpenders(PageRequest.of(0, 10)).getContent());
        report.put("mostFrequent", clientRepository.findMostFrequent(PageRequest.of(0, 10)).getContent());
        
        return report;
    }

    public Map<String, Object> getBarberReport(Integer barberId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        Map<String, Object> report = new HashMap<>();
        
        report.put("revenue", paymentRepository.sumCommissionByBarberAndDateRange(barberId, startDateTime, endDateTime));
        report.put("averageRating", reviewRepository.averageRatingByBarber(barberId));
        report.put("totalReviews", reviewRepository.countByBarber(barberId));
        report.put("ratingDistribution", reviewRepository.ratingDistributionByBarber(barberId));
        
        return report;
    }

    // === Métodos para Controller ===

    public Map<String, Object> getClientReport(LocalDate startDate, LocalDate endDate) {
        return getClientReport();
    }

    public Map<String, Object> getBarberReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        report.put("barbers", barberRepository.findAll());
        return report;
    }

    public Map<String, Object> getServicesReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        // Implementação simplificada
        return report;
    }

    public Map<String, Object> getAppointmentsToday() {
        Map<String, Object> result = new HashMap<>();
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        
        result.put("total", appointmentRepository.countByStartTimeBetween(start, end));
        result.put("date", LocalDate.now());
        return result;
    }

    public List<Map<String, Object>> getBarbersStatus() {
        return barberRepository.findAll().stream()
                .map(barber -> {
                    Map<String, Object> status = new HashMap<>();
                    status.put("id", barber.getIdBarber());
                    status.put("name", barber.getName());
                    status.put("active", barber.getActive());
                    return status;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Object> getRevenueTodayRealtime() {
        Map<String, Object> result = new HashMap<>();
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDateTime.now();
        
        Double revenue = paymentRepository.sumRevenueByDateRange(start, end);
        result.put("revenue", revenue != null ? revenue : 0.0);
        result.put("timestamp", LocalDateTime.now());
        return result;
    }

    public Map<String, Object> comparePeriods(LocalDate p1Start, LocalDate p1End, LocalDate p2Start, LocalDate p2End) {
        Map<String, Object> result = new HashMap<>();
        
        DashboardDTO period1 = getDashboard(p1Start, p1End);
        DashboardDTO period2 = getDashboard(p2Start, p2End);
        
        result.put("period1", period1);
        result.put("period2", period2);
        return result;
    }

    public Map<String, Object> compareMonthOverMonth() {
        LocalDate now = LocalDate.now();
        LocalDate thisMonthStart = now.withDayOfMonth(1);
        LocalDate lastMonthStart = thisMonthStart.minusMonths(1);
        LocalDate lastMonthEnd = thisMonthStart.minusDays(1);
        
        return comparePeriods(thisMonthStart, now, lastMonthStart, lastMonthEnd);
    }

    public Map<String, Object> compareYearOverYear() {
        LocalDate now = LocalDate.now();
        LocalDate thisYearStart = now.withDayOfYear(1);
        LocalDate lastYearStart = thisYearStart.minusYears(1);
        LocalDate lastYearEnd = thisYearStart.minusDays(1);
        
        return comparePeriods(thisYearStart, now, lastYearStart, lastYearEnd);
    }

    public List<Map<String, Object>> getRevenueTrend(int days) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(days);
        
        List<Object[]> data = paymentRepository.dailyRevenue(start, end);
        return data.stream()
                .map(row -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("date", row[0]);
                    item.put("revenue", row[1]);
                    return item;
                })
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getAppointmentsTrend(int days) {
        // Implementação simplificada
        return new ArrayList<>();
    }

    public List<Map<String, Object>> getClientsTrend(int days) {
        // Implementação simplificada
        return new ArrayList<>();
    }

    public Map<String, Object> getKPIs() {
        Map<String, Object> kpis = new HashMap<>();
        
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        DashboardDTO dashboard = getDashboard(startOfMonth, today);
        
        kpis.put("monthRevenue", dashboard.getTotalRevenue());
        kpis.put("totalClients", dashboard.getTotalClients());
        kpis.put("averageRating", dashboard.getAverageRating());
        kpis.put("occupancyRate", dashboard.getOccupancyRate());
        
        return kpis;
    }

    public Map<String, Object> getBarberKPIs(Long barberId) {
        Map<String, Object> kpis = new HashMap<>();
        
        kpis.put("averageRating", reviewRepository.averageRatingByBarber(barberId.intValue()));
        kpis.put("totalReviews", reviewRepository.countByBarber(barberId.intValue()));
        
        return kpis;
    }
}
