package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.model.Appointment;
import br.edu.ufape.gobarber.model.Barber;
import br.edu.ufape.gobarber.model.Services;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade Appointment e suas operações básicas.
 * Estes testes validam a criação, modificação e integridade dos dados de agendamentos.
 */
@SpringBootTest
public class AppointmentServiceTest {

    /**
     * Testa a criação de um agendamento com todos os campos preenchidos.
     */
    @Test
    public void testAppointmentCreation() {
        LocalDateTime startTime = LocalDateTime.of(2024, 12, 20, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 12, 20, 11, 0);

        Appointment appointment = new Appointment(
                1,
                "João Silva",
                "11-98765-4321",
                null, // barber
                null, // serviceType
                startTime,
                endTime,
                50.0
        );

        assertEquals(1, appointment.getId());
        assertEquals("João Silva", appointment.getClientName());
        assertEquals("11-98765-4321", appointment.getClientNumber());
        assertEquals(startTime, appointment.getStartTime());
        assertEquals(endTime, appointment.getEndTime());
        assertEquals(50.0, appointment.getTotalPrice());
    }

    /**
     * Testa a modificação de dados de um agendamento.
     */
    @Test
    public void testUpdateAppointmentData() {
        Appointment appointment = new Appointment();
        appointment.setClientName("Maria Santos");
        appointment.setClientNumber("11-91234-5678");
        appointment.setTotalPrice(75.0);

        assertEquals("Maria Santos", appointment.getClientName());
        assertEquals("11-91234-5678", appointment.getClientNumber());
        assertEquals(75.0, appointment.getTotalPrice());
    }

    /**
     * Testa o construtor padrão do Appointment.
     */
    @Test
    public void testDefaultConstructor() {
        Appointment appointment = new Appointment();

        assertNull(appointment.getId());
        assertNull(appointment.getClientName());
        assertNull(appointment.getClientNumber());
        assertNull(appointment.getBarber());
        assertNull(appointment.getServiceType());
        assertNull(appointment.getStartTime());
        assertNull(appointment.getEndTime());
        assertNull(appointment.getTotalPrice());
    }

    /**
     * Testa a associação de serviços ao agendamento.
     */
    @Test
    public void testAppointmentWithServices() {
        Services service1 = new Services();
        service1.setId(1);
        service1.setName("Corte de Cabelo");
        service1.setValue(30.0);
        service1.setTime(LocalTime.of(0, 30));

        Services service2 = new Services();
        service2.setId(2);
        service2.setName("Barba");
        service2.setValue(20.0);
        service2.setTime(LocalTime.of(0, 20));

        Set<Services> services = new HashSet<>();
        services.add(service1);
        services.add(service2);

        Appointment appointment = new Appointment();
        appointment.setServiceType(services);

        assertEquals(2, appointment.getServiceType().size());
        assertTrue(appointment.getServiceType().contains(service1));
        assertTrue(appointment.getServiceType().contains(service2));
    }

    /**
     * Testa a atribuição de horários de início e fim.
     */
    @Test
    public void testAppointmentTimeAssignment() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(1);

        Appointment appointment = new Appointment();
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);

        assertEquals(startTime, appointment.getStartTime());
        assertEquals(endTime, appointment.getEndTime());
        assertTrue(appointment.getEndTime().isAfter(appointment.getStartTime()));
    }

    /**
     * Testa a associação de um barbeiro ao agendamento.
     */
    @Test
    public void testAppointmentWithBarber() {
        Barber barber = new Barber();
        barber.setIdBarber(1);
        barber.setName("Carlos Barbeiro");

        Appointment appointment = new Appointment();
        appointment.setBarber(barber);

        assertNotNull(appointment.getBarber());
        assertEquals("Carlos Barbeiro", appointment.getBarber().getName());
        assertEquals(1, appointment.getBarber().getIdBarber());
    }

    /**
     * Testa a modificação do preço total do agendamento.
     */
    @Test
    public void testTotalPriceModification() {
        Appointment appointment = new Appointment();
        appointment.setTotalPrice(100.0);

        assertEquals(100.0, appointment.getTotalPrice());

        appointment.setTotalPrice(150.0);
        assertEquals(150.0, appointment.getTotalPrice());
    }

    /**
     * Testa agendamento com número de cliente em diferentes formatos.
     */
    @Test
    public void testClientNumberFormats() {
        Appointment appointment = new Appointment();
        
        appointment.setClientNumber("11987654321");
        assertEquals("11987654321", appointment.getClientNumber());

        appointment.setClientNumber("(11) 98765-4321");
        assertEquals("(11) 98765-4321", appointment.getClientNumber());

        appointment.setClientNumber("+55 11 98765-4321");
        assertEquals("+55 11 98765-4321", appointment.getClientNumber());
    }
}
