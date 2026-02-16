package br.edu.ufape.gobarber.service;

import br.edu.ufape.gobarber.dto.sale.SaleEmailDTO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private Configuration fmConfiguration;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private Template template;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() throws Exception {
        ReflectionTestUtils.setField(emailService, "email", "gobarberapi@gmail.com");
    }

    @Test
    void testSendPromotionalEmail_Success() throws Exception {
        // Arrange
        SaleEmailDTO saleDTO = new SaleEmailDTO("Promoção de Natal", 50.0, "NATAL2025", LocalDate.now().plusDays(30));

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(fmConfiguration.getTemplate(anyString())).thenReturn(template);
        doAnswer(invocation -> {
            StringWriter writer = invocation.getArgument(1);
            writer.write("<html>Promocao</html>");
            return null;
        }).when(template).process(any(), any(StringWriter.class));

        // Act
        emailService.sendPromotionalEmail("aderamos108@gmail.com", saleDTO);

        // Assert - verifica que o email foi criado
        verify(javaMailSender, times(1)).createMimeMessage();
    }

    @Test
    void testSendEmail_Success() throws Exception {
        // Arrange
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act & Assert - não deve lançar exceção
        assertDoesNotThrow(() -> 
            emailService.sendEmail("aderamos108@gmail.com", "Teste", "Conteúdo de teste")
        );
    }

    @Test
    void testSendHtmlEmail_Success() throws Exception {
        // Arrange
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act & Assert - não deve lançar exceção
        assertDoesNotThrow(() -> 
            emailService.sendHtmlEmail("aderamos108@gmail.com", "Teste HTML", "<h1>Teste</h1>")
        );
    }

    @Test
    void testSendEmail_WithValidData() throws Exception {
        // Arrange
        String recipient = "aderamos108@gmail.com";
        String subject = "Teste de Email GoBarber";
        String content = "Este é um email de teste do sistema GoBarber";

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        emailService.sendEmail(recipient, subject, content);

        // Assert
        verify(javaMailSender, times(1)).createMimeMessage();
    }

    @Test
    void testSendPromotionalEmail_WithAllFields() throws Exception {
        // Arrange
        SaleEmailDTO saleDTO = new SaleEmailDTO("Super Promoção", 99.90, "SUPER99", LocalDate.of(2025, 12, 31));

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(fmConfiguration.getTemplate("sale.ftl")).thenReturn(template);
        doAnswer(invocation -> {
            StringWriter writer = invocation.getArgument(1);
            writer.write("<html>Template</html>");
            return null;
        }).when(template).process(any(), any(StringWriter.class));

        // Act
        emailService.sendPromotionalEmail("aderamos108@gmail.com", saleDTO);

        // Assert
        verify(fmConfiguration, times(1)).getTemplate("sale.ftl");
    }
}
