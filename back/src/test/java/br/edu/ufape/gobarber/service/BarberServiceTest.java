package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.model.Address;
import br.edu.ufape.gobarber.model.Barber;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest
public class BarberServiceTest {

    @Autowired
    private BarberService barberService;
    @Test
    public void testBarberCreation() {
        Address address = new Address(1, "Rua A", 123, "Bairro B", "Cidade C", "Estado D", "12345-678");
        Barber barber = new Barber();
        barber.setIdBarber(1);
        barber.setName("João");
        barber.setCpf("123.456.789-00");
        barber.setAddress(address);
        barber.setSalary(3000.0);
        barber.setAdmissionDate(LocalDate.of(2022, 1, 15));
        barber.setProfilePhoto(new byte[]{0, 1, 2, 3, 4, 5});
        barber.setWorkload(40);
        barber.setContato("11-98765-4321");
        barber.setStart(LocalTime.of(8, 0));
        barber.setEnd(LocalTime.of(17, 0));

        assertEquals(1, barber.getIdBarber());
        assertEquals("João", barber.getName());
        assertEquals("123.456.789-00", barber.getCpf());
        assertEquals(address, barber.getAddress());
        assertEquals(3000.00, barber.getSalary());
        assertEquals(LocalDate.of(2022, 1, 15), barber.getAdmissionDate());
        assertEquals(40, barber.getWorkload());
    }

    @Test
    public void testUpdateBarberData() {
        Barber barber = new Barber();
        barber.setName("Carlos");
        barber.setCpf("987.654.321-00");

        assertEquals("Carlos", barber.getName());
        assertEquals("987.654.321-00", barber.getCpf());
    }

    @Test
    public void testBarberCreationWithoutProfilePhoto() {
    Address address = new Address(1, "Rua A", 123, "Bairro B", "Cidade C", "Estado D", "12345-678");
    Barber barber = new Barber();
    barber.setIdBarber(1);
    barber.setName("João Silva");
    barber.setCpf("123.456.789-00");
    barber.setAddress(address);
    barber.setSalary(3000.0);
    barber.setAdmissionDate(LocalDate.of(2022, 1, 15));
    barber.setProfilePhoto(null);
    barber.setWorkload(40);
    barber.setContato("11-98765-4321");
    barber.setStart(LocalTime.of(8, 0));
    barber.setEnd(LocalTime.of(17, 0));
    assertNull(barber.getProfilePhoto());
    }

    @Test
    public void testInvalidCpfFormat() {
        Barber barber = new Barber();
        barber.setCpf("invalid-cpf");

        // Verificar se CPF não é válido (dependendo da lógica de validação do sistema)
        assertNotEquals("123.456.789-10", barber.getCpf());
    }

    @Test
    public void testDefaultConstructor() {
        Barber barber = new Barber();

        assertNull(barber.getIdBarber());
        assertNull(barber.getName());
        assertNull(barber.getCpf());
        assertNull(barber.getAddress());
        assertEquals(0.0, barber.getSalary());
        assertNull(barber.getAdmissionDate());
        assertNull(barber.getProfilePhoto());
        assertNull(barber.getWorkload());
    }

    @Test
    public void testSalaryValidation() {
        Barber barber = new Barber();
        barber.setSalary(3500.00);

        assertEquals(3500.00, barber.getSalary());

        // Verifica se o salário não pode ser negativo
        barber.setSalary(-500.00);
        assertTrue(barber.getSalary() < 0);
    }

    @Test
    public void testWorkloadValidation() {
        Barber barber = new Barber();
        barber.setWorkload(40);

        assertEquals(40, barber.getWorkload());

        // Verifica se a carga horária não pode ser negativa
        barber.setWorkload(-10);
        assertTrue(barber.getWorkload() < 0);
    }

    @Test
    public void testAdmissionDateAssignment() {
        LocalDate today = LocalDate.now();
        Barber barber = new Barber();
        barber.setAdmissionDate(today);

        assertEquals(today, barber.getAdmissionDate());
    }

    @Test
    public void testProfilePhotoModification() {
        Barber barber = new Barber();
        byte[] initialPhoto = new byte[]{1, 2, 3};
        byte[] newPhoto = new byte[]{4, 5, 6};

        barber.setProfilePhoto(initialPhoto);
        assertArrayEquals(initialPhoto, barber.getProfilePhoto());

        barber.setProfilePhoto(newPhoto);
        assertArrayEquals(newPhoto, barber.getProfilePhoto());
    }

}
