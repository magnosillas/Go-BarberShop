package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.barbershop.BarbershopCreateDTO;
import br.edu.ufape.gobarber.dto.barbershop.BarbershopDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Address;
import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.model.Barbershop;
import br.edu.ufape.gobarber.model.Client;
import br.edu.ufape.gobarber.repository.BarberRepository;
import br.edu.ufape.gobarber.repository.BarbershopRepository;
import br.edu.ufape.gobarber.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BarbershopService {

    private final BarbershopRepository barbershopRepository;
    private final ClientRepository clientRepository;
    private final BarberRepository barberRepository;

    public List<BarbershopDTO> getAllBarbershops() {
        return barbershopRepository.findAll().stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    public List<BarbershopDTO> getActiveBarbershops() {
        return barbershopRepository.findByActiveTrue().stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    public BarbershopDTO getBarbershopById(Integer id) throws DataBaseException {
        Barbershop shop = barbershopRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Barbearia não encontrada."));
        return convertEntityToDTO(shop);
    }

    public BarbershopDTO getBarbershopBySlug(String slug) throws DataBaseException {
        Barbershop shop = barbershopRepository.findBySlug(slug)
                .orElseThrow(() -> new DataBaseException("Barbearia não encontrada."));
        return convertEntityToDTO(shop);
    }

    public List<BarbershopDTO> searchBarbershops(String name) {
        return barbershopRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    public List<BarbershopDTO> getBarbershopsByClient(Long clientId) {
        return barbershopRepository.findByClientId(clientId).stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retorna os barbeiros ativos vinculados a uma barbearia (multi-tenant)
     */
    public List<Barber> getBarbersBySlug(String slug) throws DataBaseException {
        if (!barbershopRepository.existsBySlug(slug)) {
            throw new DataBaseException("Barbearia não encontrada.");
        }
        return barbershopRepository.findBarbersBySlug(slug);
    }

    @Transactional
    public BarbershopDTO createBarbershop(BarbershopCreateDTO createDTO) throws DataBaseException {
        try {
            if (createDTO.getSlug() == null || createDTO.getSlug().isBlank()) {
                createDTO.setSlug(generateSlug(createDTO.getName()));
            }
            if (barbershopRepository.existsBySlug(createDTO.getSlug())) {
                throw new DataBaseException("Já existe uma barbearia com esse slug.");
            }
            if (createDTO.getCnpj() != null && !createDTO.getCnpj().isBlank()
                    && barbershopRepository.existsByCnpj(createDTO.getCnpj())) {
                throw new DataBaseException("Já existe uma barbearia com esse CNPJ.");
            }

            Barbershop shop = convertDTOtoEntity(createDTO);
            shop.setActive(true);
            shop = barbershopRepository.save(shop);
            return convertEntityToDTO(shop);
        } catch (DataBaseException e) {
            throw e;
        } catch (Exception e) {
            throw new DataBaseException("Erro ao criar barbearia: " + e.getMessage());
        }
    }

    @Transactional
    public BarbershopDTO updateBarbershop(Integer id, BarbershopCreateDTO updateDTO) throws DataBaseException {
        Barbershop shop = barbershopRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Barbearia não encontrada."));

        shop.setName(updateDTO.getName());
        if (updateDTO.getSlug() != null && !updateDTO.getSlug().isBlank()) {
            if (!shop.getSlug().equals(updateDTO.getSlug()) && barbershopRepository.existsBySlug(updateDTO.getSlug())) {
                throw new DataBaseException("Já existe uma barbearia com esse slug.");
            }
            shop.setSlug(updateDTO.getSlug());
        }
        shop.setDescription(updateDTO.getDescription());
        shop.setCnpj(updateDTO.getCnpj());
        shop.setPhone(updateDTO.getPhone());
        shop.setEmail(updateDTO.getEmail());
        shop.setLogoUrl(updateDTO.getLogoUrl());
        shop.setBannerUrl(updateDTO.getBannerUrl());
        shop.setOpeningHours(updateDTO.getOpeningHours());

        Address address = shop.getAddress();
        if (address == null) {
            address = new Address();
        }
        address.setStreet(updateDTO.getStreet());
        address.setNumber(updateDTO.getNumber());
        address.setNeighborhood(updateDTO.getNeighborhood());
        address.setCity(updateDTO.getCity());
        address.setState(updateDTO.getState());
        address.setCep(updateDTO.getCep());
        shop.setAddress(address);

        shop = barbershopRepository.save(shop);
        return convertEntityToDTO(shop);
    }

    @Transactional
    public void deleteBarbershop(Integer id) throws DataBaseException {
        Barbershop shop = barbershopRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Barbearia não encontrada."));
        barbershopRepository.delete(shop);
    }

    @Transactional
    public BarbershopDTO toggleActive(Integer id) throws DataBaseException {
        Barbershop shop = barbershopRepository.findById(id)
                .orElseThrow(() -> new DataBaseException("Barbearia não encontrada."));
        shop.setActive(!shop.getActive());
        shop = barbershopRepository.save(shop);
        return convertEntityToDTO(shop);
    }

    @Transactional
    public BarbershopDTO addClientToBarbershop(Integer barbershopId, Long clientId) throws DataBaseException {
        Barbershop shop = barbershopRepository.findById(barbershopId)
                .orElseThrow(() -> new DataBaseException("Barbearia não encontrada."));
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new DataBaseException("Cliente não encontrado."));

        shop.getClients().add(client);
        shop = barbershopRepository.save(shop);
        return convertEntityToDTO(shop);
    }

    @Transactional
    public BarbershopDTO removeClientFromBarbershop(Integer barbershopId, Long clientId) throws DataBaseException {
        Barbershop shop = barbershopRepository.findById(barbershopId)
                .orElseThrow(() -> new DataBaseException("Barbearia não encontrada."));
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new DataBaseException("Cliente não encontrado."));

        shop.getClients().remove(client);
        shop = barbershopRepository.save(shop);
        return convertEntityToDTO(shop);
    }

    @Transactional
    public BarbershopDTO addBarberToBarbershop(Integer barbershopId, Integer barberId) throws DataBaseException {
        Barbershop shop = barbershopRepository.findById(barbershopId)
                .orElseThrow(() -> new DataBaseException("Barbearia não encontrada."));
        Barber barber = barberRepository.findById(barberId)
                .orElseThrow(() -> new DataBaseException("Barbeiro não encontrado."));

        shop.getBarbers().add(barber);
        shop = barbershopRepository.save(shop);
        return convertEntityToDTO(shop);
    }

    @Transactional
    public BarbershopDTO removeBarberFromBarbershop(Integer barbershopId, Integer barberId) throws DataBaseException {
        Barbershop shop = barbershopRepository.findById(barbershopId)
                .orElseThrow(() -> new DataBaseException("Barbearia não encontrada."));
        Barber barber = barberRepository.findById(barberId)
                .orElseThrow(() -> new DataBaseException("Barbeiro não encontrado."));

        shop.getBarbers().remove(barber);
        shop = barbershopRepository.save(shop);
        return convertEntityToDTO(shop);
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[áàâã]", "a")
                .replaceAll("[éèê]", "e")
                .replaceAll("[íìî]", "i")
                .replaceAll("[óòôõ]", "o")
                .replaceAll("[úùû]", "u")
                .replaceAll("[ç]", "c")
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    private BarbershopDTO convertEntityToDTO(Barbershop shop) {
        BarbershopDTO dto = new BarbershopDTO();
        dto.setIdBarbershop(shop.getIdBarbershop());
        dto.setName(shop.getName());
        dto.setSlug(shop.getSlug());
        dto.setDescription(shop.getDescription());
        dto.setCnpj(shop.getCnpj());
        dto.setPhone(shop.getPhone());
        dto.setEmail(shop.getEmail());
        dto.setLogoUrl(shop.getLogoUrl());
        dto.setBannerUrl(shop.getBannerUrl());
        dto.setOpeningHours(shop.getOpeningHours());
        dto.setActive(shop.getActive());
        dto.setCreatedAt(shop.getCreatedAt());
        dto.setUpdatedAt(shop.getUpdatedAt());

        if (shop.getAddress() != null) {
            dto.setStreet(shop.getAddress().getStreet());
            dto.setNumber(shop.getAddress().getNumber());
            dto.setNeighborhood(shop.getAddress().getNeighborhood());
            dto.setCity(shop.getAddress().getCity());
            dto.setState(shop.getAddress().getState());
            dto.setCep(shop.getAddress().getCep());
        }

        return dto;
    }

    private Barbershop convertDTOtoEntity(BarbershopCreateDTO dto) {
        Barbershop shop = new Barbershop();
        shop.setName(dto.getName());
        shop.setSlug(dto.getSlug());
        shop.setDescription(dto.getDescription());
        shop.setCnpj(dto.getCnpj());
        shop.setPhone(dto.getPhone());
        shop.setEmail(dto.getEmail());
        shop.setLogoUrl(dto.getLogoUrl());
        shop.setBannerUrl(dto.getBannerUrl());
        shop.setOpeningHours(dto.getOpeningHours());

        if (dto.getStreet() != null && !dto.getStreet().isBlank()) {
            Address address = new Address();
            address.setStreet(dto.getStreet());
            address.setNumber(dto.getNumber());
            address.setNeighborhood(dto.getNeighborhood());
            address.setCity(dto.getCity());
            address.setState(dto.getState());
            address.setCep(dto.getCep());
            shop.setAddress(address);
        }

        return shop;
    }
}
