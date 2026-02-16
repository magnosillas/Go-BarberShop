package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.model.Address;
import br.edu.ufape.gobarber.model.Secretary;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade Secretary e suas operações básicas.
 * Estes testes validam a criação, modificação e integridade dos dados de secretários.
 */
@SpringBootTest
public class SecretaryServiceTest {

    /**
     * Testa a criação de um secretário com todos os campos preenchidos.
     */
    @Test
    public void testSecretaryCreation() {
        Address address = new Address(1, "Rua A", 123, "Bairro B", "Cidade C", "Estado D", "12345-678");
        
        Secretary secretary = new Secretary(
                1,
                "Maria Silva",
                "123.456.789-00",
                address,
                2500.0,
                LocalDate.of(2023, 1, 15),
                new byte[]{0, 1, 2, 3, 4, 5},
                40,
                "11-98765-4321",
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                null
        );

        assertEquals(1, secretary.getIdSecretary());
        assertEquals("Maria Silva", secretary.getName());
        assertEquals("123.456.789-00", secretary.getCpf());
        assertEquals(address, secretary.getAddress());
        assertEquals(2500.0, secretary.getSalary());
        assertEquals(LocalDate.of(2023, 1, 15), secretary.getAdmissionDate());
        assertEquals(40, secretary.getWorkload());
        assertEquals("11-98765-4321", secretary.getContact());
    }

    /**
     * Testa a modificação de dados de um secretário.
     */
    @Test
    public void testUpdateSecretaryData() {
        Secretary secretary = new Secretary();
        secretary.setName("Ana Santos");
        secretary.setCpf("987.654.321-00");
        secretary.setSalary(3000.0);

        assertEquals("Ana Santos", secretary.getName());
        assertEquals("987.654.321-00", secretary.getCpf());
        assertEquals(3000.0, secretary.getSalary());
    }

    /**
     * Testa o construtor padrão do Secretary.
     */
    @Test
    public void testDefaultConstructor() {
        Secretary secretary = new Secretary();

        assertNull(secretary.getIdSecretary());
        assertNull(secretary.getName());
        assertNull(secretary.getCpf());
        assertNull(secretary.getAddress());
        assertEquals(0.0, secretary.getSalary());
        assertNull(secretary.getAdmissionDate());
        assertNull(secretary.getProfilePhoto());
        assertNull(secretary.getWorkload());
        assertNull(secretary.getContact());
    }

    /**
     * Testa a criação de um secretário sem foto de perfil.
     */
    @Test
    public void testSecretaryCreationWithoutProfilePhoto() {
        Secretary secretary = new Secretary();
        secretary.setName("João Oliveira");
        secretary.setCpf("111.222.333-44");
        secretary.setProfilePhoto(null);

        assertNull(secretary.getProfilePhoto());
        assertNotNull(secretary.getName());
    }

    /**
     * Testa a validação do horário de trabalho.
     */
    @Test
    public void testWorkingHours() {
        Secretary secretary = new Secretary();
        secretary.setStart(LocalTime.of(8, 0));
        secretary.setEnd(LocalTime.of(17, 0));

        assertEquals(LocalTime.of(8, 0), secretary.getStart());
        assertEquals(LocalTime.of(17, 0), secretary.getEnd());
        assertTrue(secretary.getEnd().isAfter(secretary.getStart()));
    }

    /**
     * Testa a modificação do salário.
     */
    @Test
    public void testSalaryModification() {
        Secretary secretary = new Secretary();
        secretary.setSalary(2500.0);

        assertEquals(2500.0, secretary.getSalary());

        secretary.setSalary(3000.0);
        assertEquals(3000.0, secretary.getSalary());
    }

    /**
     * Testa a atribuição da data de admissão.
     */
    @Test
    public void testAdmissionDateAssignment() {
        LocalDate today = LocalDate.now();
        Secretary secretary = new Secretary();
        secretary.setAdmissionDate(today);

        assertEquals(today, secretary.getAdmissionDate());
    }

    /**
     * Testa a modificação da foto de perfil.
     */
    @Test
    public void testProfilePhotoModification() {
        Secretary secretary = new Secretary();
        byte[] initialPhoto = new byte[]{1, 2, 3};
        byte[] newPhoto = new byte[]{4, 5, 6};

        secretary.setProfilePhoto(initialPhoto);
        assertArrayEquals(initialPhoto, secretary.getProfilePhoto());

        secretary.setProfilePhoto(newPhoto);
        assertArrayEquals(newPhoto, secretary.getProfilePhoto());
    }

    /**
     * Testa a carga horária de trabalho.
     */
    @Test
    public void testWorkloadValidation() {
        Secretary secretary = new Secretary();
        secretary.setWorkload(44);

        assertEquals(44, secretary.getWorkload());

        secretary.setWorkload(40);
        assertEquals(40, secretary.getWorkload());
    }

    /**
     * Testa o contato do secretário.
     */
    @Test
    public void testContactAssignment() {
        Secretary secretary = new Secretary();
        
        secretary.setContact("11-98765-4321");
        assertEquals("11-98765-4321", secretary.getContact());

        secretary.setContact("(81) 99999-9999");
        assertEquals("(81) 99999-9999", secretary.getContact());
    }

    /**
     * Testa a associação de endereço ao secretário.
     */
    @Test
    public void testSecretaryWithAddress() {
        Address address = new Address();
        address.setStreet("Rua das Flores");
        address.setNumber(100);
        address.setCity("Recife");
        address.setState("PE");
        address.setCep("50000-000");

        Secretary secretary = new Secretary();
        secretary.setAddress(address);

        assertNotNull(secretary.getAddress());
        assertEquals("Rua das Flores", secretary.getAddress().getStreet());
        assertEquals("Recife", secretary.getAddress().getCity());
    }
}
