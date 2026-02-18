package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.client.ClientCreateDTO;
import br.edu.ufape.gobarber.dto.client.ClientDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.exceptions.ResourceNotFoundException;
import br.edu.ufape.gobarber.model.Client;
import br.edu.ufape.gobarber.model.Client.ContactMethod;
import br.edu.ufape.gobarber.model.Client.Gender;
import br.edu.ufape.gobarber.model.Client.LoyaltyTier;
import br.edu.ufape.gobarber.repository.BarberRepository;
import br.edu.ufape.gobarber.repository.ClientRepository;
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
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private BarberRepository barberRepository;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private ClientService clientService;

    private Client client;
    private ClientCreateDTO clientCreateDTO;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .idClient(1L)
                .name("João Silva")
                .email("joao@email.com")
                .phone("11999999999")
                .cpf("123.456.789-00")
                .birthDate(LocalDate.of(1990, 5, 15))
                .gender(Gender.MALE)
                .loyaltyPoints(100)
                .loyaltyTier(LoyaltyTier.BRONZE)
                .totalVisits(10)
                .totalSpent(500.0)
                .receivePromotions(true)
                .receiveReminders(true)
                .active(true)
                .build();

        clientCreateDTO = new ClientCreateDTO();
        clientCreateDTO.setName("João Silva");
        clientCreateDTO.setEmail("joao@email.com");
        clientCreateDTO.setPhone("11999999999");
        clientCreateDTO.setCpf("123.456.789-00");
    }

    @Test
    void testGetClientById_Success() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ClientDTO result = clientService.getClientById(1L);

        assertNotNull(result);
        assertEquals("João Silva", result.getName());
        assertEquals("joao@email.com", result.getEmail());
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void testGetClientById_NotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.getClientById(1L));
    }

    @Test
    void testGetClientByEmail_Success() {
        when(clientRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(client));

        ClientDTO result = clientService.getClientByEmail("joao@email.com");

        assertNotNull(result);
        assertEquals("João Silva", result.getName());
        verify(clientRepository, times(1)).findByEmail("joao@email.com");
    }

    @Test
    void testGetClientByEmail_NotFound() {
        when(clientRepository.findByEmail("notfound@email.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.getClientByEmail("notfound@email.com"));
    }

    @Test
    void testGetClientByPhone_Success() {
        when(clientRepository.findByPhone("11999999999")).thenReturn(Optional.of(client));

        ClientDTO result = clientService.getClientByPhone("11999999999");

        assertNotNull(result);
        assertEquals("11999999999", result.getPhone());
        verify(clientRepository, times(1)).findByPhone("11999999999");
    }

    @Test
    void testGetClientByPhone_NotFound() {
        when(clientRepository.findByPhone("00000000000")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.getClientByPhone("00000000000"));
    }

    @Test
    void testGetAllClients_Success() {
        Page<Client> clientPage = new PageImpl<>(Arrays.asList(client));
        when(clientRepository.findByActiveTrue(any(Pageable.class))).thenReturn(clientPage);

        Page<ClientDTO> result = clientService.getAllClients(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(clientRepository, times(1)).findByActiveTrue(any(Pageable.class));
    }

    @Test
    void testSearchClients_Success() {
        Page<Client> clientPage = new PageImpl<>(Arrays.asList(client));
        when(clientRepository.findByNameContainingIgnoreCase(eq("João"), any(Pageable.class))).thenReturn(clientPage);

        Page<ClientDTO> result = clientService.searchClients("João", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(clientRepository, times(1)).findByNameContainingIgnoreCase(eq("João"), any(Pageable.class));
    }

    @Test
    void testDeleteClient_Success() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        clientService.deleteClient(1L);

        verify(clientRepository, times(1)).findById(1L);
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void testDeleteClient_NotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.deleteClient(1L));
    }

    @Test
    void testAddLoyaltyPoints_Success() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientDTO result = clientService.addLoyaltyPoints(1L, 50);

        assertNotNull(result);
        verify(clientRepository, times(1)).findById(1L);
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void testAddLoyaltyPoints_NotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.addLoyaltyPoints(1L, 50));
    }

    @Test
    void testRegisterVisit_Success() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientDTO result = clientService.registerVisit(1L, 100.0);

        assertNotNull(result);
        verify(clientRepository, times(1)).findById(1L);
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void testGetClientPhoto_Success() {
        byte[] photo = new byte[]{1, 2, 3};
        client.setProfilePhoto(photo);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        byte[] result = clientService.getClientPhoto(1L);

        assertNotNull(result);
        assertArrayEquals(photo, result);
    }

    @Test
    void testGetClientPhoto_NotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.getClientPhoto(1L));
    }

    // Testes para entidade Client
    @Test
    void testClientEntity_DefaultValues() {
        Client newClient = new Client();
        
        assertEquals(0, newClient.getLoyaltyPoints());
        assertEquals(LoyaltyTier.BRONZE, newClient.getLoyaltyTier());
        assertEquals(ContactMethod.WHATSAPP, newClient.getPreferredContactMethod());
        assertTrue(newClient.getReceivePromotions());
        assertTrue(newClient.getReceiveReminders());
    }

    @Test
    void testClientEntity_SetterGetter() {
        Client newClient = new Client();
        newClient.setName("Maria");
        newClient.setEmail("maria@test.com");
        newClient.setPhone("11888888888");
        newClient.setCpf("987.654.321-00");
        newClient.setGender(Gender.FEMALE);
        newClient.setLoyaltyPoints(200);
        newClient.setLoyaltyTier(LoyaltyTier.SILVER);

        assertEquals("Maria", newClient.getName());
        assertEquals("maria@test.com", newClient.getEmail());
        assertEquals("11888888888", newClient.getPhone());
        assertEquals("987.654.321-00", newClient.getCpf());
        assertEquals(Gender.FEMALE, newClient.getGender());
        assertEquals(200, newClient.getLoyaltyPoints());
        assertEquals(LoyaltyTier.SILVER, newClient.getLoyaltyTier());
    }
}
