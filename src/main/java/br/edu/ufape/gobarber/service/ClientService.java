package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.client.ClientCreateDTO;
import br.edu.ufape.gobarber.dto.client.ClientDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.exceptions.ResourceNotFoundException;
import br.edu.ufape.gobarber.model.Address;
import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.model.Client;
import br.edu.ufape.gobarber.repository.BarberRepository;
import br.edu.ufape.gobarber.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {
    public List<ClientDTO> getClientsByPreferredBarber(Long barberId) {
        List<Client> clients = clientRepository.findByPreferredBarberId(barberId.intValue());
        return clients.stream().map(ClientDTO::fromEntity).collect(Collectors.toList());
    }

    private final ClientRepository clientRepository;
    private final BarberRepository barberRepository;
    private final AddressService addressService;

    @Transactional
    public ClientDTO createClient(ClientCreateDTO dto, MultipartFile photo) throws DataBaseException {
        // Validações de unicidade
        if (dto.getEmail() != null && clientRepository.existsByEmail(dto.getEmail())) {
            throw new DataBaseException("Email já cadastrado");
        }
        if (dto.getCpf() != null && clientRepository.existsByCpf(dto.getCpf())) {
            throw new DataBaseException("CPF já cadastrado");
        }
        if (clientRepository.existsByPhone(dto.getPhone())) {
            throw new DataBaseException("Telefone já cadastrado");
        }

        Client client = new Client();
        mapDtoToEntity(dto, client);

        if (photo != null && !photo.isEmpty()) {
            try {
                client.setProfilePhoto(photo.getBytes());
            } catch (IOException e) {
                throw new DataBaseException("Erro ao processar foto do cliente");
            }
        }

        Client saved = clientRepository.save(client);
        return ClientDTO.fromEntity(saved);
    }

    @Transactional
    public ClientDTO updateClient(Long id, ClientCreateDTO dto, MultipartFile photo) throws DataBaseException {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        // Validações de unicidade (exceto o próprio registro)
        if (dto.getEmail() != null && !dto.getEmail().equals(client.getEmail()) 
                && clientRepository.existsByEmail(dto.getEmail())) {
            throw new DataBaseException("Email já cadastrado");
        }
        if (dto.getCpf() != null && !dto.getCpf().equals(client.getCpf()) 
                && clientRepository.existsByCpf(dto.getCpf())) {
            throw new DataBaseException("CPF já cadastrado");
        }

        mapDtoToEntity(dto, client);

        if (photo != null && !photo.isEmpty()) {
            try {
                client.setProfilePhoto(photo.getBytes());
            } catch (IOException e) {
                throw new DataBaseException("Erro ao processar foto do cliente");
            }
        }

        Client saved = clientRepository.save(client);
        return ClientDTO.fromEntity(saved);
    }

    @Transactional
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        client.setActive(false);
        clientRepository.save(client);
    }

    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        return ClientDTO.fromEntity(client);
    }

    public ClientDTO getClientByEmail(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        return ClientDTO.fromEntity(client);
    }

    public ClientDTO getClientByPhone(String phone) {
        Client client = clientRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        return ClientDTO.fromEntity(client);
    }

    public Page<ClientDTO> getAllClients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return clientRepository.findByActiveTrue(pageable).map(ClientDTO::fromEntity);
    }

    public Page<ClientDTO> searchClients(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return clientRepository.findByNameContainingIgnoreCase(name, pageable).map(ClientDTO::fromEntity);
    }

    public byte[] getClientPhoto(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        return client.getProfilePhoto();
    }

    // === Fidelidade ===

    @Transactional
    public ClientDTO addLoyaltyPoints(Long clientId, int points) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        client.addLoyaltyPoints(points);
        return ClientDTO.fromEntity(clientRepository.save(client));
    }

    @Transactional
    public ClientDTO registerVisit(Long clientId, double amountSpent) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        client.registerVisit(amountSpent);
        return ClientDTO.fromEntity(clientRepository.save(client));
    }

    public List<ClientDTO> getClientsByLoyaltyTier(Client.LoyaltyTier tier) {
        return clientRepository.findByLoyaltyTier(tier).stream()
                .map(ClientDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ClientDTO> getTopSpenders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return clientRepository.findTopSpenders(pageable).map(ClientDTO::fromEntity);
    }

    public Page<ClientDTO> getMostFrequentClients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return clientRepository.findMostFrequent(pageable).map(ClientDTO::fromEntity);
    }

    // === Marketing ===

    public List<ClientDTO> getBirthdayClients() {
        LocalDate today = LocalDate.now();
        return clientRepository.findBirthdayClients(today.getMonthValue(), today.getDayOfMonth())
                .stream()
                .map(ClientDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ClientDTO> getInactiveClients(int daysInactive) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysInactive);
        return clientRepository.findInactiveClients(cutoffDate).stream()
                .map(ClientDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ClientDTO> getClientsForPromotions() {
        return clientRepository.findByReceivePromotionsTrue().stream()
                .map(ClientDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // === Estatísticas ===

    public Long countNewClients(int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        return clientRepository.countNewClients(startDate);
    }

    public List<Object[]> getClientsByLoyaltyTierStats() {
        return clientRepository.countByLoyaltyTier();
    }

    // === Métodos para Controller ===

    public ClientDTO create(ClientCreateDTO dto, MultipartFile photo) throws DataBaseException {
        return createClient(dto, photo);
    }

    public ClientDTO update(Long id, ClientCreateDTO dto, MultipartFile photo) throws DataBaseException {
        return updateClient(id, dto, photo);
    }

    public void delete(Long id) {
        deleteClient(id);
    }

    public ClientDTO findById(Long id) {
        return getClientById(id);
    }

    public Page<ClientDTO> findAll(Pageable pageable) {
        return clientRepository.findByActiveTrue(pageable).map(ClientDTO::fromEntity);
    }

    public ClientDTO findByEmail(String email) {
        return getClientByEmail(email);
    }

    public ClientDTO findByPhone(String phone) {
        return getClientByPhone(phone);
    }

    public ClientDTO findByCpf(String cpf) {
        Client client = clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        return ClientDTO.fromEntity(client);
    }

    public List<ClientDTO> searchByName(String name) {
        return clientRepository.findByNameContainingIgnoreCase(name, PageRequest.of(0, 100))
                .map(ClientDTO::fromEntity)
                .getContent();
    }

    public byte[] getPhoto(Long id) {
        return getClientPhoto(id);
    }

    public void updatePhoto(Long id, MultipartFile photo) throws DataBaseException {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        try {
            client.setProfilePhoto(photo.getBytes());
            clientRepository.save(client);
        } catch (IOException e) {
            throw new DataBaseException("Erro ao processar foto");
        }
    }

    public void deletePhoto(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        client.setProfilePhoto(null);
        clientRepository.save(client);
    }

    public ClientDTO addLoyaltyPoints(Long id, int points, String reason) {
        return addLoyaltyPoints(id, points);
    }

    public ClientDTO redeemLoyaltyPoints(Long id, int points) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        if (client.getLoyaltyPoints() < points) {
            throw new IllegalArgumentException("Pontos insuficientes");
        }
        client.addLoyaltyPoints(-points);
        return ClientDTO.fromEntity(clientRepository.save(client));
    }


    public List<ClientDTO> getTopClients(int limit) {
        return clientRepository.findTopSpenders(PageRequest.of(0, limit))
                .map(ClientDTO::fromEntity)
                .getContent();
    }

    public List<ClientDTO> getVipClients() {
        return clientRepository.findByLoyaltyTier(Client.LoyaltyTier.PLATINUM).stream()
                .map(ClientDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ClientDTO> getClientsWithBirthdayThisMonth() {
        int month = LocalDate.now().getMonthValue();
        return clientRepository.findAll().stream()
                .filter(c -> c.getBirthDate() != null && c.getBirthDate().getMonthValue() == month)
                .map(ClientDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // === Helpers ===

    private void mapDtoToEntity(ClientCreateDTO dto, Client client) throws DataBaseException {
        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        client.setCpf(dto.getCpf());
        client.setBirthDate(dto.getBirthDate());
        client.setGender(dto.getGender());
        client.setNotes(dto.getNotes());

        if (dto.getPreferredContactMethod() != null) {
            client.setPreferredContactMethod(dto.getPreferredContactMethod());
        }
        if (dto.getReceivePromotions() != null) {
            client.setReceivePromotions(dto.getReceivePromotions());
        }
        if (dto.getReceiveReminders() != null) {
            client.setReceiveReminders(dto.getReceiveReminders());
        }

        // Barbeiro preferido
        if (dto.getPreferredBarberId() != null) {
            Barber barber = barberRepository.findById(dto.getPreferredBarberId())
                    .orElseThrow(() -> new ResourceNotFoundException("Barbeiro não encontrado"));
            client.setPreferredBarber(barber);
        }

        // Endereço
        if (dto.getAddress() != null) {
            if (client.getAddress() == null) {
                Address address = addressService.createAddressEntity(dto.getAddress());
                client.setAddress(address);
            } else {
                addressService.updateAddressEntity(client.getAddress(), dto.getAddress());
            }
        }
    }
}
