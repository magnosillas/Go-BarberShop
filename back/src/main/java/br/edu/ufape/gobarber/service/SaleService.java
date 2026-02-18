package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.page.PageSaleDTO;
import br.edu.ufape.gobarber.dto.sale.SaleCreateDTO;
import br.edu.ufape.gobarber.dto.sale.SaleDTO;
import br.edu.ufape.gobarber.dto.sale.SaleEmailDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseConstraintException;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.model.Client;
import br.edu.ufape.gobarber.model.Sale;
import br.edu.ufape.gobarber.repository.ClientRepository;
import br.edu.ufape.gobarber.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleService {


    private final SaleRepository saleRepository;
    private final EmailService emailService;
    private final ClientRepository clientRepository;
    private final NotificationService notificationService;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Transactional
    public SaleDTO createSale(SaleCreateDTO saleCreateDTO) throws DataBaseException, DataBaseConstraintException, ConstraintViolationException {
        if(saleCreateDTO.getCoupon() != null && saleCreateDTO.getCoupon().length() != 7){
            throw new IllegalArgumentException("O cupom deve ter exatamente 7 caracteres.");
        }

        try {
            Sale sale = new Sale();
            sale.setName(saleCreateDTO.getName());
            sale.setTotalPrice(saleCreateDTO.getTotalPrice());
            sale.setStartDate(saleCreateDTO.getStartDate());
            sale.setEndDate(saleCreateDTO.getEndDate());

            if (saleCreateDTO.getCoupon() != null) {
                sale.setCoupon(saleCreateDTO.getCoupon());
            } else {
                sale.setCoupon(generateCoupon(sale.getName()));
            }

            sale = saleRepository.save(sale);

            return convertSaleToDTO(sale);

        } catch (Exception e){
            if(e.getMessage().contains("constraint [sale_discount_coupon_key]")) {
                throw new DataBaseConstraintException("Cupom já cadastrado no banco de dados.");
            } else {
                throw new DataBaseException("Erro interno no banco de dados. Tente Novamente!");
            }
        }
    }

    @Transactional
    public SaleDTO updateSale(Integer id, SaleCreateDTO saleCreateDTO) throws DataBaseException {

        Sale sale = saleRepository.findById(id).orElseThrow(() -> new DataBaseException("Promoção não encontrada no banco de dados"));

        if(saleCreateDTO.getCoupon() != null && saleCreateDTO.getCoupon().length() != 7){
            throw new IllegalArgumentException("O cupom deve ter exatamente 7 caracteres.");
        }

        sale.setName(saleCreateDTO.getName());
        sale.setTotalPrice(saleCreateDTO.getTotalPrice());
        sale.setStartDate(saleCreateDTO.getStartDate());
        sale.setEndDate(saleCreateDTO.getEndDate());
        sale.setCoupon(saleCreateDTO.getCoupon());

        sale = saleRepository.save(sale);

        return convertSaleToDTO(sale);
    }

    @Transactional
    public void delete(Integer id) {
        Optional<Sale> sale = saleRepository.findById(id);

        sale.ifPresent(saleRepository::delete);
    }

    public PageSaleDTO getAllSales(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Sale> salePage = saleRepository.findAll(pageable);
        Page<SaleDTO> saleDTOPage = salePage.map(this::convertSaleToDTO);

        return new PageSaleDTO(
                saleDTOPage.getTotalElements(),
                saleDTOPage.getTotalPages(),
                saleDTOPage.getPageable().getPageNumber(),
                saleDTOPage.getSize(),
                saleDTOPage.getContent()
        );
    }

    public PageSaleDTO getAllValidSales(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Sale> salePage = saleRepository.findAllByEndDateAfter(LocalDate.now(), pageable);
        Page<SaleDTO> saleDTOPage = salePage.map(this::convertSaleToDTO);

        return new PageSaleDTO(
                saleDTOPage.getTotalElements(),
                saleDTOPage.getTotalPages(),
                saleDTOPage.getPageable().getPageNumber(),
                saleDTOPage.getSize(),
                saleDTOPage.getContent()
        );
    }

    public SaleDTO getSale(Integer id) throws DataBaseException {
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new DataBaseException("Promoção não encontrada!"));

        return convertSaleToDTO(sale);
    }

    public SaleDTO getSaleByCoupon(String coupon) throws DataBaseException {
        Sale sale = saleRepository.findByCoupon(coupon).orElseThrow(() -> new DataBaseException("Promoção não encontrada!"));

        return convertSaleToDTO(sale);
    }

    private SaleDTO convertSaleToDTO (Sale sale) {
        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setId(sale.getIdSale());
        saleDTO.setName(sale.getName());
        saleDTO.setCoupon(sale.getCoupon());
        saleDTO.setTotalPrice(sale.getTotalPrice());
        saleDTO.setStartDate(sale.getStartDate());
        saleDTO.setEndDate(sale.getEndDate());

        return saleDTO;
    }

    private String generateCoupon(String saleName) {
        // Pegue o dia e o mes para gerar o cupom
        LocalDate now = LocalDate.now();
        String dayAndMonth = String.format("%02d%02d", now.getDayOfMonth(), now.getMonthValue());

        // Extraia os caracteres unicos do nome da promoção
        Set<Character> uniqueLetters = new HashSet<>();
        for (char c : saleName.toCharArray()) {
            if (Character.isLetter(c) && !Character.isWhitespace(c)) {
                uniqueLetters.add(Character.toUpperCase(c));
            }
        }

        // Verifique se possui ao menos 3 caracteres unicos, se não, adicione Z para completar
        List<Character> lettersList = new ArrayList<>(uniqueLetters);
        while (lettersList.size() < 3) {
            lettersList.add('Z');
        }

        // Selecione 3 letras aleatórias da lista de letras unicas
        Collections.shuffle(lettersList, RANDOM);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            result.append(lettersList.get(i));
        }

        // adicione o dia e o mes ao fim do cupom
        result.append(dayAndMonth);

        // Converta para um array de char e embaralhe os caracteres
        char[] resultArray = result.toString().toCharArray();
        List<Character> list = new ArrayList<>();
        for(Character c : resultArray){
            list.add(c);
        }
        Collections.shuffle(list, RANDOM);
        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i] = list.get(i);
        }
        String generatedCoupon = new String(resultArray);

        for(int i = 0; i < 10; i ++) {
            if (saleRepository.findByCoupon(generatedCoupon).isPresent()) {
                Collections.shuffle(list, RANDOM);
                for (int j = 0; j < resultArray.length; j++) {
                    resultArray[j] = list.get(j);
                }
                generatedCoupon = new String(resultArray);
            } else {
                break;
            }
        }

        if(saleRepository.findByCoupon(generatedCoupon).isPresent()){
            generatedCoupon = generateCoupon(saleName);
        }

        return generatedCoupon;
    }

    public void sendPromotionalEmail(Integer idSale) {
        Sale sale = saleRepository.findById(idSale)
                .orElseThrow(() -> new RuntimeException("Promoção não encontrada com o id: " + idSale));

        SaleEmailDTO saleEmailDTO = new SaleEmailDTO(sale.getName(), sale.getTotalPrice(), sale.getCoupon(), sale.getEndDate());

        // Busca emails dos clientes que aceitam promoções
        List<String> costumersEmails = clientRepository.findByReceivePromotionsTrue()
                .stream()
                .map(Client::getEmail)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        for(String email : costumersEmails) {
            emailService.sendPromotionalEmail(email, saleEmailDTO);
        }

        // Cria notificações no sistema para os clientes
        notificationService.createPromotionNotifications(sale);
    }

}
