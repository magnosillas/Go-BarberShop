package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.payment.PaymentCreateDTO;
import br.edu.ufape.gobarber.dto.payment.PaymentDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.exceptions.ResourceNotFoundException;
import br.edu.ufape.gobarber.model.*;
import br.edu.ufape.gobarber.repository.*;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private BarberRepository barberRepository;

    @Mock
    private SaleRepository saleRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Payment payment;
    private Client client;
    private Barber barber;
    private Appointment appointment;
    private Sale sale;
    private PaymentCreateDTO paymentCreateDTO;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setIdClient(1L);
        client.setName("João Silva");
        client.setEmail("aderamos108@gmail.com");
        client.setLoyaltyPoints(100);
        client.setLoyaltyTier(Client.LoyaltyTier.SILVER);

        barber = new Barber();
        barber.setIdBarber(1);
        barber.setName("Carlos Barbeiro");

        appointment = new Appointment();
        appointment.setId(1);
        appointment.setBarber(barber);
        appointment.setStartTime(LocalDateTime.now());
        appointment.setTotalPrice(50.0);

        sale = new Sale();
        sale.setIdSale(1);
        sale.setName("Promoção");
        sale.setCoupon("PROMO2025");
        sale.setEndDate(LocalDate.now().plusDays(30));
        sale.setTotalPrice(10.0);

        payment = Payment.builder()
                .idPayment(1L)
                .amount(100.0)
                .finalAmount(100.0)
                .paymentMethod(Payment.PaymentMethod.CREDIT_CARD)
                .status(Payment.PaymentStatus.PENDING)
                .client(client)
                .barber(barber)
                .appointment(appointment)
                .loyaltyPointsEarned(10)
                .createdAt(LocalDateTime.now())
                .build();

        paymentCreateDTO = new PaymentCreateDTO();
        paymentCreateDTO.setAmount(100.0);
        paymentCreateDTO.setPaymentMethod(Payment.PaymentMethod.CREDIT_CARD);
        paymentCreateDTO.setClientId(1L);
        paymentCreateDTO.setBarberId(1);
        paymentCreateDTO.setAppointmentId(1);
    }

    @Test
    void testCreatePayment_CashPayment_Success() throws DataBaseException {
        paymentCreateDTO.setPaymentMethod(Payment.PaymentMethod.CASH);
        
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(barberRepository.findById(1)).thenReturn(Optional.of(barber));
        when(appointmentRepository.findById(1)).thenReturn(Optional.of(appointment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        PaymentDTO result = paymentService.createPayment(paymentCreateDTO);

        assertNotNull(result);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testCreatePayment_WithCoupon_Success() throws DataBaseException {
        paymentCreateDTO.setCouponCode("PROMO2025");
        
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(barberRepository.findById(1)).thenReturn(Optional.of(barber));
        when(appointmentRepository.findById(1)).thenReturn(Optional.of(appointment));
        when(saleRepository.findByCoupon("PROMO2025")).thenReturn(Optional.of(sale));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentDTO result = paymentService.createPayment(paymentCreateDTO);

        assertNotNull(result);
        verify(saleRepository, times(1)).findByCoupon("PROMO2025");
    }

    @Test
    void testCreatePayment_WithExpiredCoupon_ThrowsException() {
        paymentCreateDTO.setCouponCode("EXPIRED");
        sale.setEndDate(LocalDate.now().minusDays(1)); // Cupom expirado
        
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(barberRepository.findById(1)).thenReturn(Optional.of(barber));
        when(appointmentRepository.findById(1)).thenReturn(Optional.of(appointment));
        when(saleRepository.findByCoupon("EXPIRED")).thenReturn(Optional.of(sale));

        assertThrows(DataBaseException.class, () -> 
            paymentService.createPayment(paymentCreateDTO)
        );
    }

    @Test
    void testCreatePayment_WithInvalidCoupon_ThrowsException() {
        paymentCreateDTO.setCouponCode("INVALID");
        
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(barberRepository.findById(1)).thenReturn(Optional.of(barber));
        when(appointmentRepository.findById(1)).thenReturn(Optional.of(appointment));
        when(saleRepository.findByCoupon("INVALID")).thenReturn(Optional.empty());

        assertThrows(DataBaseException.class, () -> 
            paymentService.createPayment(paymentCreateDTO)
        );
    }

    @Test
    void testCreatePayment_PIX_Success() throws DataBaseException {
        paymentCreateDTO.setPaymentMethod(Payment.PaymentMethod.PIX);
        
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(barberRepository.findById(1)).thenReturn(Optional.of(barber));
        when(appointmentRepository.findById(1)).thenReturn(Optional.of(appointment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentDTO result = paymentService.createPayment(paymentCreateDTO);

        assertNotNull(result);
    }

    @Test
    void testConfirmPayment_Success() throws DataBaseException {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        PaymentDTO result = paymentService.confirmPayment(1L, "TXN123456");

        assertNotNull(result);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testConfirmPayment_NotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            paymentService.confirmPayment(1L, "TXN123456")
        );
    }

    @Test
    void testConfirmPayment_AlreadyProcessed() {
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        assertThrows(DataBaseException.class, () -> 
            paymentService.confirmPayment(1L, "TXN123456")
        );
    }

    @Test
    void testCancelPayment_Success() throws DataBaseException {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentDTO result = paymentService.cancelPayment(1L);

        assertNotNull(result);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testRefundPayment_Success() throws DataBaseException {
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentDTO result = paymentService.refundPayment(1L, "Reembolso total");

        assertNotNull(result);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testFindById_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        PaymentDTO result = paymentService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdPayment());
    }

    @Test
    void testFindById_NotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            paymentService.findById(1L)
        );
    }

    @Test
    void testFindByClient_Success() {
        Page<Payment> page = new PageImpl<>(Arrays.asList(payment));
        when(paymentRepository.findByClientIdClient(eq(1L), any(Pageable.class))).thenReturn(page);

        List<PaymentDTO> result = paymentService.findByClient(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByBarber_Success() {
        Page<Payment> page = new PageImpl<>(Arrays.asList(payment));
        when(paymentRepository.findByBarberIdBarber(eq(1), any(Pageable.class))).thenReturn(page);

        List<PaymentDTO> result = paymentService.findByBarber(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByStatus_Success() {
        Page<Payment> page = new PageImpl<>(Arrays.asList(payment));
        when(paymentRepository.findByStatus(eq(Payment.PaymentStatus.PENDING), any(Pageable.class))).thenReturn(page);

        List<PaymentDTO> result = paymentService.findByStatus(Payment.PaymentStatus.PENDING);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testPaymentMethod_AllValues() {
        Payment.PaymentMethod[] methods = Payment.PaymentMethod.values();
        assertTrue(methods.length >= 4);
        assertNotNull(Payment.PaymentMethod.CASH);
        assertNotNull(Payment.PaymentMethod.CREDIT_CARD);
        assertNotNull(Payment.PaymentMethod.DEBIT_CARD);
        assertNotNull(Payment.PaymentMethod.PIX);
    }

    @Test
    void testPaymentStatus_AllValues() {
        Payment.PaymentStatus[] statuses = Payment.PaymentStatus.values();
        assertTrue(statuses.length >= 4);
        assertNotNull(Payment.PaymentStatus.PENDING);
        assertNotNull(Payment.PaymentStatus.COMPLETED);
        assertNotNull(Payment.PaymentStatus.CANCELLED);
        assertNotNull(Payment.PaymentStatus.REFUNDED);
    }
}
