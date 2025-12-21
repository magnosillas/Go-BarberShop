package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.doc.DashboardControllerDoc;
import br.edu.ufape.gobarber.dto.dashboard.DashboardDTO;
import br.edu.ufape.gobarber.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController implements DashboardControllerDoc {

    private final DashboardService dashboardService;

    @Override
    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboard(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(dashboardService.getDashboard(startDate.toLocalDate(), endDate.toLocalDate()));
    }

    @Override
    @GetMapping("/today")
    public ResponseEntity<DashboardDTO> getTodayDashboard() {
        LocalDate today = LocalDate.now();
        return ResponseEntity.ok(dashboardService.getDashboard(today, today));
    }

    @Override
    @GetMapping("/week")
    public ResponseEntity<DashboardDTO> getWeekDashboard() {
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(7);
        return ResponseEntity.ok(dashboardService.getDashboard(weekAgo, today));
    }

    @Override
    @GetMapping("/month")
    public ResponseEntity<DashboardDTO> getMonthDashboard() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        return ResponseEntity.ok(dashboardService.getDashboard(startOfMonth, today));
    }

    @Override
    @GetMapping("/year")
    public ResponseEntity<DashboardDTO> getYearDashboard() {
        LocalDate today = LocalDate.now();
        LocalDate startOfYear = today.withDayOfYear(1);
        return ResponseEntity.ok(dashboardService.getDashboard(startOfYear, today));
    }

    @Override
    @GetMapping("/financial")
    public ResponseEntity<Map<String, Object>> getFinancialReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(dashboardService.getFinancialReport(startDate.toLocalDate(), endDate.toLocalDate()));
    }

    @Override
    @GetMapping("/clients")
    public ResponseEntity<Map<String, Object>> getClientReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(dashboardService.getClientReport(startDate.toLocalDate(), endDate.toLocalDate()));
    }

    @Override
    @GetMapping("/barbers")
    public ResponseEntity<Map<String, Object>> getBarberReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(dashboardService.getBarberReport(startDate.toLocalDate(), endDate.toLocalDate()));
    }

    @Override
    @GetMapping("/services-report")
    public ResponseEntity<Map<String, Object>> getServicesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(dashboardService.getServicesReport(startDate.toLocalDate(), endDate.toLocalDate()));
    }

    @Override
    @GetMapping("/appointments-today")
    public ResponseEntity<Map<String, Object>> getAppointmentsToday() {
        return ResponseEntity.ok(dashboardService.getAppointmentsToday());
    }

    @Override
    @GetMapping("/barbers-status")
    public ResponseEntity<List<Map<String, Object>>> getBarbersStatus() {
        return ResponseEntity.ok(dashboardService.getBarbersStatus());
    }

    @Override
    @GetMapping("/revenue-realtime")
    public ResponseEntity<Map<String, Object>> getRevenueTodayRealtime() {
        return ResponseEntity.ok(dashboardService.getRevenueTodayRealtime());
    }

    @Override
    @GetMapping("/compare")
    public ResponseEntity<Map<String, Object>> comparePeriods(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime period1Start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime period1End,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime period2Start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime period2End) {
        return ResponseEntity.ok(dashboardService.comparePeriods(
                period1Start.toLocalDate(), period1End.toLocalDate(),
                period2Start.toLocalDate(), period2End.toLocalDate()));
    }

    @Override
    @GetMapping("/compare-mom")
    public ResponseEntity<Map<String, Object>> compareMonthOverMonth() {
        return ResponseEntity.ok(dashboardService.compareMonthOverMonth());
    }

    @Override
    @GetMapping("/compare-yoy")
    public ResponseEntity<Map<String, Object>> compareYearOverYear() {
        return ResponseEntity.ok(dashboardService.compareYearOverYear());
    }

    @Override
    @GetMapping("/trend/revenue")
    public ResponseEntity<List<Map<String, Object>>> getRevenueTrend(
            @RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(dashboardService.getRevenueTrend(days));
    }

    @Override
    @GetMapping("/trend/appointments")
    public ResponseEntity<List<Map<String, Object>>> getAppointmentsTrend(
            @RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(dashboardService.getAppointmentsTrend(days));
    }

    @Override
    @GetMapping("/trend/clients")
    public ResponseEntity<List<Map<String, Object>>> getClientsTrend(
            @RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(dashboardService.getClientsTrend(days));
    }

    @Override
    @GetMapping("/kpis")
    public ResponseEntity<Map<String, Object>> getKPIs() {
        return ResponseEntity.ok(dashboardService.getKPIs());
    }

    @Override
    @GetMapping("/barber/{barberId}/kpis")
    public ResponseEntity<Map<String, Object>> getBarberKPIs(@PathVariable Long barberId) {
        return ResponseEntity.ok(dashboardService.getBarberKPIs(barberId));
    }
}
