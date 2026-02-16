package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.model.AuditLog;
import br.edu.ufape.gobarber.repository.AuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuditService Tests")
class AuditServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditService auditService;

    private AuditLog auditLog;

    @BeforeEach
    void setUp() {
        auditLog = new AuditLog();
        auditLog.setIdAuditLog(1L);
        auditLog.setUserId(5L);
        auditLog.setUserEmail("admin@gobarber.com");
        auditLog.setAction(AuditLog.AuditAction.LOGIN);
        auditLog.setEntityType("User");
        auditLog.setEntityId(5L);
        auditLog.setDescription("Usuário realizou login");
        auditLog.setIpAddress("192.168.1.100");
        auditLog.setUserAgent("Mozilla/5.0");
        auditLog.setCreatedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("Log Action Tests")
    class LogActionTests {

        @Test
        @DisplayName("Deve registrar ação genérica")
        void shouldLogGenericAction() {
            when(auditLogRepository.save(any(AuditLog.class))).thenReturn(auditLog);

            auditService.logAction(
                    5L,
                    "admin@gobarber.com",
                    AuditLog.AuditAction.CREATE,
                    "Appointment",
                    10L,
                    "Criou agendamento",
                    "192.168.1.100",
                    "Mozilla/5.0");

            verify(auditLogRepository).save(any(AuditLog.class));
        }

        @Test
        @DisplayName("Deve registrar login")
        void shouldLogLogin() {
            when(auditLogRepository.save(any(AuditLog.class))).thenReturn(auditLog);

            auditService.logLogin(5L, "admin@gobarber.com", "192.168.1.100", "Mozilla/5.0");

            verify(auditLogRepository).save(argThat(log -> 
                    log.getAction() == AuditLog.AuditAction.LOGIN));
        }

        @Test
        @DisplayName("Deve registrar logout")
        void shouldLogLogout() {
            when(auditLogRepository.save(any(AuditLog.class))).thenReturn(auditLog);

            auditService.logLogout(5L, "admin@gobarber.com", "192.168.1.100", "Mozilla/5.0");

            verify(auditLogRepository).save(argThat(log -> 
                    log.getAction() == AuditLog.AuditAction.LOGOUT));
        }

        @Test
        @DisplayName("Deve registrar falha de login")
        void shouldLogLoginFailed() {
            when(auditLogRepository.save(any(AuditLog.class))).thenReturn(auditLog);

            auditService.logLoginFailed("hacker@evil.com", "192.168.1.100", "Mozilla/5.0");

            verify(auditLogRepository).save(argThat(log -> 
                    log.getAction() == AuditLog.AuditAction.LOGIN_FAILED));
        }

        @Test
        @DisplayName("Deve registrar pagamento")
        void shouldLogPayment() {
            when(auditLogRepository.save(any(AuditLog.class))).thenReturn(auditLog);

            auditService.logPayment(
                    5L,
                    "client@email.com",
                    10L,
                    150.0,
                    "192.168.1.100",
                    "Mozilla/5.0");

            verify(auditLogRepository).save(argThat(log -> 
                    log.getAction() == AuditLog.AuditAction.PAYMENT));
        }

        @Test
        @DisplayName("Deve registrar reembolso")
        void shouldLogRefund() {
            when(auditLogRepository.save(any(AuditLog.class))).thenReturn(auditLog);

            auditService.logRefund(
                    5L,
                    "admin@gobarber.com",
                    10L,
                    150.0,
                    "Solicitação do cliente",
                    "192.168.1.100",
                    "Mozilla/5.0");

            verify(auditLogRepository).save(argThat(log -> 
                    log.getAction() == AuditLog.AuditAction.REFUND));
        }
    }

    @Nested
    @DisplayName("Query Audit Logs Tests")
    class QueryAuditLogsTests {

        @Test
        @DisplayName("Deve retornar logs por usuário")
        void shouldReturnLogsByUser() {
            when(auditLogRepository.findByUserIdOrderByCreatedAtDesc(5L))
                    .thenReturn(Arrays.asList(auditLog));

            List<AuditLog> result = auditService.getLogsByUser(5L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getUserId()).isEqualTo(5L);
        }

        @Test
        @DisplayName("Deve retornar logs por ação")
        void shouldReturnLogsByAction() {
            when(auditLogRepository.findByActionOrderByCreatedAtDesc(AuditLog.AuditAction.LOGIN))
                    .thenReturn(Arrays.asList(auditLog));

            List<AuditLog> result = auditService.getLogsByAction(AuditLog.AuditAction.LOGIN);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getAction()).isEqualTo(AuditLog.AuditAction.LOGIN);
        }

        @Test
        @DisplayName("Deve retornar logs por entidade")
        void shouldReturnLogsByEntity() {
            when(auditLogRepository.findByEntityTypeAndEntityIdOrderByCreatedAtDesc("User", 5L))
                    .thenReturn(Arrays.asList(auditLog));

            List<AuditLog> result = auditService.getLogsByEntity("User", 5L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getEntityType()).isEqualTo("User");
        }

        @Test
        @DisplayName("Deve retornar logs por período")
        void shouldReturnLogsByPeriod() {
            LocalDateTime start = LocalDateTime.now().minusDays(7);
            LocalDateTime end = LocalDateTime.now();
            
            when(auditLogRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end))
                    .thenReturn(Arrays.asList(auditLog));

            List<AuditLog> result = auditService.getLogsByPeriod(start, end);

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Deve retornar logs por IP")
        void shouldReturnLogsByIp() {
            when(auditLogRepository.findByIpAddressOrderByCreatedAtDesc("192.168.1.100"))
                    .thenReturn(Arrays.asList(auditLog));

            List<AuditLog> result = auditService.getLogsByIpAddress("192.168.1.100");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getIpAddress()).isEqualTo("192.168.1.100");
        }

        @Test
        @DisplayName("Deve retornar logs paginados")
        void shouldReturnPaginatedLogs() {
            Page<AuditLog> page = new PageImpl<>(Arrays.asList(auditLog), PageRequest.of(0, 10), 1);
            when(auditLogRepository.findAllByOrderByCreatedAtDesc(any(PageRequest.class)))
                    .thenReturn(page);

            Page<AuditLog> result = auditService.getAllLogs(PageRequest.of(0, 10));

            assertThat(result.getContent()).hasSize(1);
        }

        @Test
        @DisplayName("Deve retornar falhas de login recentes")
        void shouldReturnRecentLoginFailures() {
            auditLog.setAction(AuditLog.AuditAction.LOGIN_FAILED);
            when(auditLogRepository.findByActionAndCreatedAtAfterOrderByCreatedAtDesc(
                    eq(AuditLog.AuditAction.LOGIN_FAILED), any(LocalDateTime.class)))
                    .thenReturn(Arrays.asList(auditLog));

            List<AuditLog> result = auditService.getRecentLoginFailures(24);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getAction()).isEqualTo(AuditLog.AuditAction.LOGIN_FAILED);
        }

        @Test
        @DisplayName("Deve contar falhas de login por IP")
        void shouldCountLoginFailuresByIp() {
            when(auditLogRepository.countByActionAndIpAddressAndCreatedAtAfter(
                    eq(AuditLog.AuditAction.LOGIN_FAILED), eq("192.168.1.100"), any(LocalDateTime.class)))
                    .thenReturn(5L);

            Long count = auditService.countLoginFailuresByIp("192.168.1.100", 1);

            assertThat(count).isEqualTo(5L);
        }
    }

    @Nested
    @DisplayName("Cleanup Tests")
    class CleanupTests {

        @Test
        @DisplayName("Deve limpar logs antigos")
        void shouldCleanOldLogs() {
            auditService.cleanOldLogs(90);

            verify(auditLogRepository).deleteByCreatedAtBefore(any(LocalDateTime.class));
        }
    }

    @Nested
    @DisplayName("Security Monitoring Tests")
    class SecurityMonitoringTests {

        @Test
        @DisplayName("Deve retornar atividade suspeita")
        void shouldReturnSuspiciousActivity() {
            when(auditLogRepository.findSuspiciousActivityByIp(anyInt(), any(LocalDateTime.class)))
                    .thenReturn(Arrays.asList(auditLog));

            List<AuditLog> result = auditService.getSuspiciousActivity(5, 1);

            assertThat(result).isNotEmpty();
        }

        @Test
        @DisplayName("Deve retornar transações de alto valor")
        void shouldReturnHighValueTransactions() {
            when(auditLogRepository.findByActionInAndCreatedAtAfterOrderByCreatedAtDesc(
                    anyList(), any(LocalDateTime.class)))
                    .thenReturn(Arrays.asList(auditLog));

            List<AuditLog> result = auditService.getHighValueTransactions(1000.0, 7);

            assertThat(result).isNotEmpty();
        }
    }
}
