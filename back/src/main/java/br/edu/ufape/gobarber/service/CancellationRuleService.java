package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.cancellationrule.CancellationRuleCreateDTO;
import br.edu.ufape.gobarber.dto.cancellationrule.CancellationRuleDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.CancellationRule;
import br.edu.ufape.gobarber.repository.CancellationRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CancellationRuleService {

    private final CancellationRuleRepository cancellationRuleRepository;

    public CancellationRuleDTO getActiveRule() throws DataBaseException {
        CancellationRule rule = cancellationRuleRepository.findFirstByActiveTrue()
                .orElseThrow(() -> new DataBaseException("Nenhuma regra de cancelamento ativa encontrada."));
        return convertEntityToDTO(rule);
    }

    public CancellationRuleDTO getRuleById(Integer id) throws DataBaseException {
        CancellationRule rule = cancellationRuleRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Regra de cancelamento não encontrada."));
        return convertEntityToDTO(rule);
    }

    public List<CancellationRuleDTO> getAllRules() {
        return cancellationRuleRepository.findAll().stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CancellationRuleDTO createRule(CancellationRuleCreateDTO createDTO) throws DataBaseException {
        try {
            CancellationRule rule = convertDTOtoEntity(createDTO);
            rule.setActive(true);
            rule = cancellationRuleRepository.save(rule);
            return convertEntityToDTO(rule);
        } catch (Exception e) {
            throw new DataBaseException("Erro ao criar regra de cancelamento: " + e.getMessage());
        }
    }

    @Transactional
    public CancellationRuleDTO updateRule(Integer id, CancellationRuleCreateDTO updateDTO) throws DataBaseException {
        CancellationRule rule = cancellationRuleRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Regra de cancelamento não encontrada."));

        rule.setCancelDeadlineHours(updateDTO.getCancelDeadlineHours());
        rule.setCancellationFeePercentage(updateDTO.getCancellationFeePercentage());
        rule.setNoShowFeePercentage(updateDTO.getNoShowFeePercentage());
        rule.setMaxCancellationsPerMonth(updateDTO.getMaxCancellationsPerMonth());
        rule.setAllowReschedule(updateDTO.getAllowReschedule());
        rule.setRescheduleDeadlineHours(updateDTO.getRescheduleDeadlineHours());
        rule.setPenaltyAfterMaxCancellations(updateDTO.getPenaltyAfterMaxCancellations());
        rule.setBlockDaysAfterMaxCancellations(updateDTO.getBlockDaysAfterMaxCancellations());

        rule = cancellationRuleRepository.save(rule);
        return convertEntityToDTO(rule);
    }

    @Transactional
    public void deleteRule(Integer id) throws DataBaseException {
        CancellationRule rule = cancellationRuleRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Regra de cancelamento não encontrada."));
        cancellationRuleRepository.delete(rule);
    }

    @Transactional
    public CancellationRuleDTO toggleActive(Integer id) throws DataBaseException {
        CancellationRule rule = cancellationRuleRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Regra de cancelamento não encontrada."));
        rule.setActive(!rule.getActive());
        rule = cancellationRuleRepository.save(rule);
        return convertEntityToDTO(rule);
    }

    private CancellationRule convertDTOtoEntity(CancellationRuleCreateDTO dto) {
        CancellationRule rule = new CancellationRule();
        rule.setCancelDeadlineHours(dto.getCancelDeadlineHours());
        rule.setCancellationFeePercentage(dto.getCancellationFeePercentage());
        rule.setNoShowFeePercentage(dto.getNoShowFeePercentage());
        rule.setMaxCancellationsPerMonth(dto.getMaxCancellationsPerMonth());
        rule.setAllowReschedule(dto.getAllowReschedule());
        rule.setRescheduleDeadlineHours(dto.getRescheduleDeadlineHours());
        rule.setPenaltyAfterMaxCancellations(dto.getPenaltyAfterMaxCancellations());
        rule.setBlockDaysAfterMaxCancellations(dto.getBlockDaysAfterMaxCancellations());
        return rule;
    }

    private CancellationRuleDTO convertEntityToDTO(CancellationRule rule) {
        CancellationRuleDTO dto = new CancellationRuleDTO();
        dto.setId(rule.getId());
        dto.setCancelDeadlineHours(rule.getCancelDeadlineHours());
        dto.setCancellationFeePercentage(rule.getCancellationFeePercentage());
        dto.setNoShowFeePercentage(rule.getNoShowFeePercentage());
        dto.setMaxCancellationsPerMonth(rule.getMaxCancellationsPerMonth());
        dto.setAllowReschedule(rule.getAllowReschedule());
        dto.setRescheduleDeadlineHours(rule.getRescheduleDeadlineHours());
        dto.setPenaltyAfterMaxCancellations(rule.getPenaltyAfterMaxCancellations());
        dto.setBlockDaysAfterMaxCancellations(rule.getBlockDaysAfterMaxCancellations());
        dto.setActive(rule.getActive());
        dto.setCreatedAt(rule.getCreatedAt());
        dto.setUpdatedAt(rule.getUpdatedAt());
        return dto;
    }
}
