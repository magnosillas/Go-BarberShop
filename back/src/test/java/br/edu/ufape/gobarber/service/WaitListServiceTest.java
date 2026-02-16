package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.model.Client;
import br.edu.ufape.gobarber.model.Services;
import br.edu.ufape.gobarber.model.WaitList;
import br.edu.ufape.gobarber.repository.BarberRepository;
import br.edu.ufape.gobarber.repository.ClientRepository;
import br.edu.ufape.gobarber.repository.ServicesRepository;
import br.edu.ufape.gobarber.repository.WaitListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WaitListServiceTest {

    @Mock
    private WaitListRepository waitListRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private BarberRepository barberRepository;

    @Mock
    private ServicesRepository servicesRepository;

    @InjectMocks
    private WaitListService waitListService;

    private WaitList waitListEntry;
    private Client client;
    private Barber barber;
    private Services service;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setIdClient(1L);
        client.setName("João Silva");

        barber = new Barber();
        barber.setIdBarber(1);
        barber.setName("Carlos Barbeiro");

        service = new Services();
        service.setId(1);
        service.setName("Corte de Cabelo");

        waitListEntry = WaitList.builder()
            .idWaitList(1L)
            .client(client)
            .barber(barber)
            .desiredTime(LocalDateTime.now().plusDays(1))
            .desiredDuration(30)
            .priority(WaitList.WaitListPriority.NORMAL)
            .status(WaitList.WaitListStatus.WAITING)
            .notes("Preferência por manhã")
            .build();
    }

    @Test
    void testAddToWaitList_Success() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(barberRepository.findById(1)).thenReturn(Optional.of(barber));
        when(waitListRepository.existsByClientIdClientAndStatusAndBarberIdBarber(any(), any(), any())).thenReturn(false);
        when(waitListRepository.save(any(WaitList.class))).thenReturn(waitListEntry);

        WaitList result = waitListService.addToWaitList(
                1L, 1L, 1,
                LocalDateTime.now().plusDays(1),
                true, 0, "Preferência por manhã"
        );

        assertNotNull(result);
        assertEquals(WaitList.WaitListStatus.WAITING, result.getStatus());
        verify(waitListRepository, times(1)).save(any(WaitList.class));
    }

    @Test
    void testAddToWaitList_ClientNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            waitListService.addToWaitList(1L, 1L, 1, LocalDateTime.now(), true, 0, null)
        );
    }

    @Test
    void testAddToWaitList_AlreadyInWaitList() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(barberRepository.findById(1)).thenReturn(Optional.of(barber));
        when(waitListRepository.existsByClientIdClientAndStatusAndBarberIdBarber(
                eq(1L), eq(WaitList.WaitListStatus.WAITING), eq(1L))).thenReturn(true);

        assertThrows(RuntimeException.class, () -> 
            waitListService.addToWaitList(1L, 1L, 1, LocalDateTime.now(), true, 0, null)
        );
    }

    @Test
    void testAddToWaitList_WithoutBarber() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(waitListRepository.save(any(WaitList.class))).thenReturn(waitListEntry);

        WaitList result = waitListService.addToWaitList(
                1L, null, 1,
                LocalDateTime.now().plusDays(1),
                true, 0, null
        );

        assertNotNull(result);
        verify(waitListRepository, times(1)).save(any(WaitList.class));
    }

    @Test
    void testNotifyClientAboutAvailability_Success() {
        when(waitListRepository.findById(1L)).thenReturn(Optional.of(waitListEntry));
        when(waitListRepository.save(any(WaitList.class))).thenReturn(waitListEntry);

        waitListService.notifyClientAboutAvailability(1L);

        verify(waitListRepository, times(1)).save(any(WaitList.class));
    }

    @Test
    void testNotifyClientAboutAvailability_NotFound() {
        when(waitListRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            waitListService.notifyClientAboutAvailability(1L)
        );
    }

    @Test
    void testConvertToAppointment_Success() {
        when(waitListRepository.findById(1L)).thenReturn(Optional.of(waitListEntry));
        when(waitListRepository.save(any(WaitList.class))).thenReturn(waitListEntry);

        waitListService.convertToAppointment(1L);

        verify(waitListRepository, times(1)).save(any(WaitList.class));
    }

    @Test
    void testConvertToAppointment_NotFound() {
        when(waitListRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            waitListService.convertToAppointment(1L)
        );
    }

    @Test
    void testCancelWaitListEntry_Success() {
        when(waitListRepository.findById(1L)).thenReturn(Optional.of(waitListEntry));
        when(waitListRepository.save(any(WaitList.class))).thenReturn(waitListEntry);

        waitListService.cancelWaitListEntry(1L);

        verify(waitListRepository, times(1)).save(any(WaitList.class));
    }

    @Test
    void testCancelWaitListEntry_NotFound() {
        when(waitListRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            waitListService.cancelWaitListEntry(1L)
        );
    }

    // Testes da entidade WaitList
    @Test
    void testWaitListEntity_DefaultValues() {
        WaitList newEntry = new WaitList();
        assertEquals(WaitList.WaitListPriority.NORMAL, newEntry.getPriority());
        assertEquals(WaitList.WaitListStatus.WAITING, newEntry.getStatus());
    }

    @Test
    void testWaitListEntity_NotifyAvailability() {
        WaitList entry = new WaitList();
        entry.setStatus(WaitList.WaitListStatus.WAITING);
        
        entry.notifyAvailability();
        
        assertEquals(WaitList.WaitListStatus.NOTIFIED, entry.getStatus());
        assertNotNull(entry.getNotifiedAt());
    }

    @Test
    void testWaitListEntity_ConvertToAppointment() {
        WaitList entry = new WaitList();
        entry.setStatus(WaitList.WaitListStatus.WAITING);
        
        entry.convertToAppointment();
        
        assertEquals(WaitList.WaitListStatus.CONVERTED, entry.getStatus());
    }

    @Test
    void testWaitListEntity_SetterGetter() {
        WaitList entry = new WaitList();
        LocalDateTime preferredDate = LocalDateTime.now().plusDays(2);
        
        entry.setClient(client);
        entry.setBarber(barber);
        entry.setDesiredTime(preferredDate);
        entry.setDesiredDuration(30);
        entry.setPriority(WaitList.WaitListPriority.HIGH);
        entry.setNotes("Urgente");
        entry.setStatus(WaitList.WaitListStatus.NOTIFIED);

        assertEquals(client, entry.getClient());
        assertEquals(barber, entry.getBarber());
        assertEquals(preferredDate, entry.getDesiredTime());
        assertEquals(30, entry.getDesiredDuration());
        assertEquals(WaitList.WaitListPriority.HIGH, entry.getPriority());
        assertEquals("Urgente", entry.getNotes());
        assertEquals(WaitList.WaitListStatus.NOTIFIED, entry.getStatus());
    }

    @Test
    void testWaitListStatus_AllValues() {
        assertEquals(5, WaitList.WaitListStatus.values().length);
        assertNotNull(WaitList.WaitListStatus.WAITING);
        assertNotNull(WaitList.WaitListStatus.NOTIFIED);
        assertNotNull(WaitList.WaitListStatus.CONVERTED);
        assertNotNull(WaitList.WaitListStatus.EXPIRED);
        assertNotNull(WaitList.WaitListStatus.CANCELLED);
    }

    @Test
    void testWaitListPriority_AllValues() {
        assertEquals(4, WaitList.WaitListPriority.values().length);
        assertNotNull(WaitList.WaitListPriority.LOW);
        assertNotNull(WaitList.WaitListPriority.NORMAL);
        assertNotNull(WaitList.WaitListPriority.HIGH);
        assertNotNull(WaitList.WaitListPriority.URGENT);
    }
}
