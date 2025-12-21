package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.waitlist.WaitListDTO;
import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.model.Client;
import br.edu.ufape.gobarber.model.WaitList;
import br.edu.ufape.gobarber.repository.BarberRepository;
import br.edu.ufape.gobarber.repository.ClientRepository;
import br.edu.ufape.gobarber.repository.WaitListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WaitListService {

    private final WaitListRepository waitListRepository;
    private final ClientRepository clientRepository;
    private final BarberRepository barberRepository;

    // === Adicionar à Lista de Espera ===

    @Transactional
    public WaitList addToWaitList(Long clientId, Long barberId, Integer serviceId,
                                   LocalDateTime preferredDate, Boolean flexibleDate,
                                   Integer priority, String notes) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Barber barber = null;
        if (barberId != null) {
            barber = barberRepository.findById(barberId.intValue())
                    .orElse(null);
        }

        // Verifica se cliente já está na fila para o mesmo barbeiro
        if (barberId != null && waitListRepository.existsByClientIdClientAndStatusAndBarberIdBarber(
                clientId, WaitList.WaitListStatus.WAITING, barberId)) {
            throw new RuntimeException("Cliente já está na lista de espera para este barbeiro");
        }

        // Calcula próxima posição
        Integer maxPosition = waitListRepository.findMaxPosition();
        int nextPosition = (maxPosition != null ? maxPosition : 0) + 1;

        WaitList.WaitListPriority waitlistPriority = WaitList.WaitListPriority.NORMAL;
        if (priority != null) {
            if (priority == 1) waitlistPriority = WaitList.WaitListPriority.LOW;
            else if (priority == 2) waitlistPriority = WaitList.WaitListPriority.HIGH;
            else if (priority >= 3) waitlistPriority = WaitList.WaitListPriority.URGENT;
        }

        WaitList entry = WaitList.builder()
                .client(client)
                .barber(barber)
                .desiredTime(preferredDate != null ? preferredDate : LocalDateTime.now().plusDays(1))
                .desiredDuration(60) // duração padrão de 60 min
                .priority(waitlistPriority)
                .status(WaitList.WaitListStatus.WAITING)
                .position(nextPosition)
                .notes(notes)
                .notified(false)
                .expirationTime(LocalDateTime.now().plusDays(7))
                .build();

        WaitList saved = waitListRepository.save(entry);
        
        log.info("Cliente {} adicionado à lista de espera na posição {}", client.getName(), nextPosition);
        
        return saved;
    }

    // === Notificação ===

    @Transactional
    public void notifyClientAboutAvailability(Long waitListId) {
        WaitList entry = waitListRepository.findById(waitListId)
                .orElseThrow(() -> new RuntimeException("Entrada na lista de espera não encontrada"));

        entry.notifyAvailability();
        waitListRepository.save(entry);

        log.info("Notificando cliente {} sobre disponibilidade", entry.getClient().getName());
    }

    // === Conversão para Agendamento ===

    @Transactional
    public void convertToAppointment(Long waitListId) {
        WaitList entry = waitListRepository.findById(waitListId)
                .orElseThrow(() -> new RuntimeException("Entrada na lista de espera não encontrada"));

        entry.convertToAppointment();
        waitListRepository.save(entry);

        log.info("Cliente {} convertido para agendamento", entry.getClient().getName());
    }

    // === Cancelamento ===

    @Transactional
    public void cancelWaitListEntry(Long waitListId) {
        WaitList entry = waitListRepository.findById(waitListId)
                .orElseThrow(() -> new RuntimeException("Entrada na lista de espera não encontrada"));

        entry.setStatus(WaitList.WaitListStatus.CANCELLED);
        waitListRepository.save(entry);

        log.info("Entrada {} cancelada", waitListId);
    }

    // === Processamento Automático ===

    @Transactional
    public void processExpiredEntries() {
        List<WaitList> expiredEntries = waitListRepository.findExpired(LocalDateTime.now());

        for (WaitList entry : expiredEntries) {
            entry.setStatus(WaitList.WaitListStatus.EXPIRED);
            waitListRepository.save(entry);
            log.info("Entrada {} expirou", entry.getIdWaitList());
        }
    }

    // === Consultas ===

    public Optional<WaitList> getById(Long id) {
        return waitListRepository.findById(id);
    }

    public WaitListDTO getWaitListById(Long id) {
        WaitList waitList = waitListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrada na lista de espera não encontrada"));
        return WaitListDTO.fromEntity(waitList);
    }

    public List<WaitList> getActiveWaitList() {
        return waitListRepository.findByStatus(WaitList.WaitListStatus.WAITING);
    }

    public Page<WaitList> getWaitListByClient(Long clientId, Pageable pageable) {
        return waitListRepository.findByClientIdClient(clientId, pageable);
    }

    public Page<WaitList> getWaitListByBarber(Long barberId, Pageable pageable) {
        return waitListRepository.findByBarberIdBarber(barberId, pageable);
    }

    public List<WaitList> getWaitingByBarber(Long barberId) {
        return waitListRepository.findWaitingByBarber(barberId);
    }

    public List<WaitList> getWaitingByService(Integer serviceId) {
        // Service não é mais campo direto no wait_list
        return waitListRepository.findByStatus(WaitList.WaitListStatus.WAITING);
    }

    public List<WaitList> getFlexibleWaitingUntil(LocalDateTime date) {
        return waitListRepository.findWaitingUntil(date);
    }

    // === Estatísticas ===

    public Long getTotalWaitingCount() {
        return waitListRepository.countTotalWaiting();
    }

    public Long getWaitingCountByBarber(Long barberId) {
        return waitListRepository.countWaitingByBarber(barberId);
    }

    public List<Object[]> getWaitingGroupByBarber() {
        return waitListRepository.countWaitingGroupByBarber();
    }

    // === Atualização ===

    @Transactional
    public WaitList updatePriority(Long waitListId, Integer newPriority) {
        WaitList entry = waitListRepository.findById(waitListId)
                .orElseThrow(() -> new RuntimeException("Entrada na lista de espera não encontrada"));

        WaitList.WaitListPriority waitlistPriority = WaitList.WaitListPriority.NORMAL;
        if (newPriority != null) {
            if (newPriority == 1) waitlistPriority = WaitList.WaitListPriority.LOW;
            else if (newPriority == 2) waitlistPriority = WaitList.WaitListPriority.HIGH;
            else if (newPriority >= 3) waitlistPriority = WaitList.WaitListPriority.URGENT;
        }

        entry.setPriority(waitlistPriority);
        return waitListRepository.save(entry);
    }

    @Transactional
    public WaitList updateNotes(Long waitListId, String notes) {
        WaitList entry = waitListRepository.findById(waitListId)
                .orElseThrow(() -> new RuntimeException("Entrada na lista de espera não encontrada"));

        entry.setNotes(notes);
        return waitListRepository.save(entry);
    }
}
