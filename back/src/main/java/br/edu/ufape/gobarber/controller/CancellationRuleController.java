package br.edu.ufape.gobarber.controller;

import br.edu.ufape.gobarber.dto.cancellationrule.CancellationRuleCreateDTO;
import br.edu.ufape.gobarber.dto.cancellationrule.CancellationRuleDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.service.CancellationRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cancellation-rules")
@RequiredArgsConstructor
@Tag(name = "Regras de Cancelamento", description = "Gerenciamento das regras de cancelamento de agendamentos")
public class CancellationRuleController {

    private final CancellationRuleService cancellationRuleService;

    @GetMapping("/active")
    public ResponseEntity<CancellationRuleDTO> getActiveRule() throws DataBaseException {
        CancellationRuleDTO rule = cancellationRuleService.getActiveRule();
        return ResponseEntity.ok(rule);
    }

    @GetMapping
    public ResponseEntity<List<CancellationRuleDTO>> getAllRules() {
        List<CancellationRuleDTO> rules = cancellationRuleService.getAllRules();
        return ResponseEntity.ok(rules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CancellationRuleDTO> getRuleById(@PathVariable Integer id) throws DataBaseException {
        CancellationRuleDTO rule = cancellationRuleService.getRuleById(id);
        return ResponseEntity.ok(rule);
    }

    @PostMapping
    public ResponseEntity<CancellationRuleDTO> createRule(@Valid @RequestBody CancellationRuleCreateDTO createDTO) throws DataBaseException {
        CancellationRuleDTO rule = cancellationRuleService.createRule(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(rule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CancellationRuleDTO> updateRule(
            @PathVariable Integer id,
            @Valid @RequestBody CancellationRuleCreateDTO updateDTO) throws DataBaseException {
        CancellationRuleDTO rule = cancellationRuleService.updateRule(id, updateDTO);
        return ResponseEntity.ok(rule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable Integer id) throws DataBaseException {
        cancellationRuleService.deleteRule(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/toggle")
    public ResponseEntity<CancellationRuleDTO> toggleActive(@PathVariable Integer id) throws DataBaseException {
        CancellationRuleDTO rule = cancellationRuleService.toggleActive(id);
        return ResponseEntity.ok(rule);
    }
}
