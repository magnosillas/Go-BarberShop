package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.model.AuditLog;
import br.edu.ufape.gobarber.repository.AuditLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    // === Registro de Ações ===

    @Async
    @Transactional
    public void logAction(Long userId, String userEmail, AuditLog.AuditAction action,
                          String entityType, Long entityId, String description,
                          String ipAddress, String userAgent) {
        try {
            AuditLog auditLog = AuditLog.builder()
                    .userId(userId)
                    .userEmail(userEmail)
                    .action(action)
                    .entityType(entityType)
                    .entityId(entityId)
                    .description(description)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .success(true)
                    .build();

            auditLogRepository.save(auditLog);
            log.debug("Audit log registrado: {} {} {} por {}", action, entityType, entityId, userEmail);
        } catch (Exception e) {
            log.error("Erro ao registrar audit log: {}", e.getMessage());
        }
    }

    // === Atalhos para Tipos Comuns ===

    public void logLogin(Long userId, String userEmail, String ipAddress, String userAgent) {
        logAction(userId, userEmail, AuditLog.AuditAction.LOGIN, "User", userId, 
                "Usuário realizou login", ipAddress, userAgent);
    }

    public void logLogout(Long userId, String userEmail, String ipAddress, String userAgent) {
        logAction(userId, userEmail, AuditLog.AuditAction.LOGOUT, "User", userId, 
                "Usuário realizou logout", ipAddress, userAgent);
    }

    public void logLoginFailed(String userEmail, String ipAddress, String userAgent) {
        logAction(null, userEmail, AuditLog.AuditAction.LOGIN_FAILED, "User", null, 
                "Tentativa de login falhou para: " + userEmail, ipAddress, userAgent);
    }

    public void logCreate(Long userId, String userEmail, String entityType, Long entityId, 
                         String ipAddress, String userAgent) {
        logAction(userId, userEmail, AuditLog.AuditAction.CREATE, entityType, entityId, 
                "Criou " + entityType + " #" + entityId, ipAddress, userAgent);
    }

    public void logUpdate(Long userId, String userEmail, String entityType, Long entityId, 
                         String ipAddress, String userAgent) {
        logAction(userId, userEmail, AuditLog.AuditAction.UPDATE, entityType, entityId, 
                "Atualizou " + entityType + " #" + entityId, ipAddress, userAgent);
    }

    public void logDelete(Long userId, String userEmail, String entityType, Long entityId, 
                         String ipAddress, String userAgent) {
        logAction(userId, userEmail, AuditLog.AuditAction.DELETE, entityType, entityId, 
                "Removeu " + entityType + " #" + entityId, ipAddress, userAgent);
    }

    public void logPayment(Long userId, String userEmail, Long paymentId, Double amount, 
                          String ipAddress, String userAgent) {
        logAction(userId, userEmail, AuditLog.AuditAction.PAYMENT, "Payment", paymentId, 
                "Pagamento de R$ " + amount + " processado", ipAddress, userAgent);
    }

    public void logRefund(Long userId, String userEmail, Long paymentId, Double amount, 
                         String reason, String ipAddress, String userAgent) {
        logAction(userId, userEmail, AuditLog.AuditAction.REFUND, "Payment", paymentId, 
                "Reembolso de R$ " + amount + " - Motivo: " + reason, ipAddress, userAgent);
    }

    // === Consultas ===

    public List<AuditLog> getLogsByUser(Long userId) {
        return auditLogRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<AuditLog> getLogsByAction(AuditLog.AuditAction action) {
        return auditLogRepository.findByActionOrderByCreatedAtDesc(action);
    }

    public List<AuditLog> getLogsByEntity(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityIdOrderByCreatedAtDesc(entityType, entityId);
    }

    public List<AuditLog> getLogsByPeriod(LocalDateTime start, LocalDateTime end) {
        return auditLogRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end);
    }

    public List<AuditLog> getLogsByIpAddress(String ipAddress) {
        return auditLogRepository.findByIpAddressOrderByCreatedAtDesc(ipAddress);
    }

    public Page<AuditLog> getAllLogs(Pageable pageable) {
        return auditLogRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public List<AuditLog> getRecentLoginFailures(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return auditLogRepository.findByActionAndCreatedAtAfterOrderByCreatedAtDesc(
                AuditLog.AuditAction.LOGIN_FAILED, since);
    }

    public Long countLoginFailuresByIp(String ipAddress, int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return auditLogRepository.countByActionAndIpAddressAndCreatedAtAfter(
                AuditLog.AuditAction.LOGIN_FAILED, ipAddress, since);
    }

    // === Segurança ===

    public List<AuditLog> getSuspiciousActivity(int threshold, int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return auditLogRepository.findSuspiciousActivityByIp(threshold, since);
    }

    public List<AuditLog> getHighValueTransactions(Double minAmount, int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return auditLogRepository.findByActionInAndCreatedAtAfterOrderByCreatedAtDesc(
                Arrays.asList(AuditLog.AuditAction.PAYMENT, AuditLog.AuditAction.REFUND), since);
    }

    // === Limpeza ===

    @Transactional
    public void cleanOldLogs(int retentionDays) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retentionDays);
        auditLogRepository.deleteByCreatedAtBefore(cutoffDate);
        log.info("Removidos logs de auditoria anteriores a {}", cutoffDate);
    }

    // === Helpers ===

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getName();
        }
        return "ANONYMOUS";
    }

    private String convertToJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Erro ao converter objeto para JSON: {}", e.getMessage());
            return obj.toString();
        }
    }
}
