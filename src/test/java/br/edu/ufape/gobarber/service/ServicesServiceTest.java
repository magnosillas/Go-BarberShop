package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.model.Services;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade Services e suas operações básicas.
 * Estes testes validam a criação, modificação e integridade dos dados de serviços.
 */
@SpringBootTest
public class ServicesServiceTest {

    /**
     * Testa a criação de um serviço com todos os campos preenchidos.
     */
    @Test
    public void testServiceCreation() {
        Services service = new Services(
                1,
                "Corte de Cabelo",
                "Corte masculino tradicional",
                35.0,
                LocalTime.of(0, 30),
                null
        );

        assertEquals(1, service.getId());
        assertEquals("Corte de Cabelo", service.getName());
        assertEquals("Corte masculino tradicional", service.getDescription());
        assertEquals(35.0, service.getValue());
        assertEquals(LocalTime.of(0, 30), service.getTime());
    }

    /**
     * Testa a modificação de dados de um serviço.
     */
    @Test
    public void testUpdateServiceData() {
        Services service = new Services();
        service.setName("Barba");
        service.setDescription("Barba completa com navalha");
        service.setValue(25.0);

        assertEquals("Barba", service.getName());
        assertEquals("Barba completa com navalha", service.getDescription());
        assertEquals(25.0, service.getValue());
    }

    /**
     * Testa o construtor padrão do Services.
     */
    @Test
    public void testDefaultConstructor() {
        Services service = new Services();

        assertNull(service.getId());
        assertNull(service.getName());
        assertNull(service.getDescription());
        assertEquals(0.0, service.getValue());
        assertNull(service.getTime());
        assertNull(service.getBarbers());
    }

    /**
     * Testa a definição do tempo de serviço.
     */
    @Test
    public void testServiceTimeAssignment() {
        Services service = new Services();
        service.setTime(LocalTime.of(1, 0));

        assertEquals(LocalTime.of(1, 0), service.getTime());
    }

    /**
     * Testa diferentes durações de serviço.
     */
    @Test
    public void testDifferentServiceDurations() {
        Services quickService = new Services();
        quickService.setName("Corte Rápido");
        quickService.setTime(LocalTime.of(0, 15));

        Services longService = new Services();
        longService.setName("Tratamento Completo");
        longService.setTime(LocalTime.of(2, 0));

        assertEquals(LocalTime.of(0, 15), quickService.getTime());
        assertEquals(LocalTime.of(2, 0), longService.getTime());
    }

    /**
     * Testa a modificação do preço do serviço.
     */
    @Test
    public void testPriceModification() {
        Services service = new Services();
        service.setValue(30.0);

        assertEquals(30.0, service.getValue());

        service.setValue(40.0);
        assertEquals(40.0, service.getValue());
    }

    /**
     * Testa serviços com valores diferentes.
     */
    @Test
    public void testServicePricing() {
        Services basicService = new Services();
        basicService.setName("Corte Simples");
        basicService.setValue(25.0);

        Services premiumService = new Services();
        premiumService.setName("Corte Premium");
        premiumService.setValue(50.0);

        assertTrue(premiumService.getValue() > basicService.getValue());
    }

    /**
     * Testa a descrição do serviço.
     */
    @Test
    public void testServiceDescription() {
        Services service = new Services();
        service.setDescription("Serviço de alta qualidade com produtos importados");

        assertNotNull(service.getDescription());
        assertTrue(service.getDescription().length() > 0);
    }

    /**
     * Testa nomes de serviços com caracteres especiais.
     */
    @Test
    public void testServiceNameWithSpecialCharacters() {
        Services service = new Services();
        service.setName("Corte & Barba");

        assertEquals("Corte & Barba", service.getName());

        service.setName("Hidratação (Completa)");
        assertEquals("Hidratação (Completa)", service.getName());
    }

    /**
     * Testa serviço com preço zero.
     */
    @Test
    public void testServiceWithZeroPrice() {
        Services service = new Services();
        service.setValue(0.0);

        assertEquals(0.0, service.getValue());
    }

    /**
     * Testa a igualdade entre serviços.
     */
    @Test
    public void testServiceInequality() {
        Services service1 = new Services();
        service1.setId(1);
        service1.setName("Corte");

        Services service2 = new Services();
        service2.setId(2);
        service2.setName("Barba");

        assertNotEquals(service1.getId(), service2.getId());
        assertNotEquals(service1.getName(), service2.getName());
    }
}
