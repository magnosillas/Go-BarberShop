package br.edu.ufape.gobarber.doc;

import br.edu.ufape.gobarber.dto.page.PageDTO;
import br.edu.ufape.gobarber.dto.payment.PaymentCreateDTO;
import br.edu.ufape.gobarber.dto.payment.PaymentDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.exceptions.NotFoundException;
import br.edu.ufape.gobarber.model.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Tag(name = "Pagamentos", description = "API para gerenciamento de pagamentos e relatórios financeiros")
public interface PaymentControllerDoc {

    @Operation(summary = "Criar pagamento", description = "Registra um novo pagamento para um agendamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pagamento criado com sucesso",
                    content = @Content(schema = @Schema(implementation = PaymentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    })
    ResponseEntity<PaymentDTO> create(PaymentCreateDTO dto) throws DataBaseException;

    @Operation(summary = "Buscar pagamento", description = "Retorna os dados de um pagamento específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento encontrado"),
            @ApiResponse(responseCode = "404", description = "Pagamento não encontrado")
    })
    ResponseEntity<PaymentDTO> findById(@Parameter(description = "ID do pagamento") Long id) throws NotFoundException;

    @Operation(summary = "Listar pagamentos", description = "Retorna lista paginada de todos os pagamentos")
    ResponseEntity<PageDTO<PaymentDTO>> findAll(Pageable pageable);

    // === Operações de Pagamento ===

    @Operation(summary = "Confirmar pagamento", description = "Confirma um pagamento pendente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento confirmado"),
            @ApiResponse(responseCode = "404", description = "Pagamento não encontrado"),
            @ApiResponse(responseCode = "400", description = "Pagamento não pode ser confirmado")
    })
    ResponseEntity<PaymentDTO> confirm(
            @Parameter(description = "ID do pagamento") Long id,
            @Parameter(description = "ID da transação externa") String transactionId) throws NotFoundException;

    @Operation(summary = "Cancelar pagamento", description = "Cancela um pagamento pendente")
    ResponseEntity<PaymentDTO> cancel(@Parameter(description = "ID do pagamento") Long id) throws NotFoundException;

    @Operation(summary = "Estornar pagamento", description = "Realiza estorno total de um pagamento")
    ResponseEntity<PaymentDTO> refund(Long id, String reason) throws NotFoundException;

    @Operation(summary = "Estorno parcial", description = "Realiza estorno parcial de um pagamento")
    ResponseEntity<PaymentDTO> partialRefund(Long id, double amount, String reason) throws NotFoundException;

    // === PIX ===

    @Operation(summary = "Gerar código PIX", description = "Gera código PIX copia-e-cola para pagamento")
    ResponseEntity<String> getPixCode(@Parameter(description = "ID do pagamento") Long id) throws NotFoundException;

    @Operation(summary = "Gerar QR Code PIX", description = "Gera QR Code PIX em base64 para pagamento")
    ResponseEntity<String> getPixQrCode(@Parameter(description = "ID do pagamento") Long id) throws NotFoundException;

    // === Consultas ===

    @Operation(summary = "Pagamentos por status", description = "Lista pagamentos por status")
    ResponseEntity<List<PaymentDTO>> findByStatus(
            @Parameter(description = "Status do pagamento") Payment.PaymentStatus status);

    @Operation(summary = "Pagamentos por método", description = "Lista pagamentos por método de pagamento")
    ResponseEntity<List<PaymentDTO>> findByMethod(
            @Parameter(description = "Método de pagamento") Payment.PaymentMethod method);

    @Operation(summary = "Pagamento do agendamento", description = "Busca pagamento de um agendamento")
    ResponseEntity<PaymentDTO> findByAppointment(
            @Parameter(description = "ID do agendamento") Long appointmentId) throws NotFoundException;

    @Operation(summary = "Pagamentos do cliente", description = "Lista pagamentos de um cliente")
    ResponseEntity<List<PaymentDTO>> findByClient(@Parameter(description = "ID do cliente") Long clientId);

    @Operation(summary = "Pagamentos do barbeiro", description = "Lista pagamentos de um barbeiro")
    ResponseEntity<List<PaymentDTO>> findByBarber(@Parameter(description = "ID do barbeiro") Long barberId);

    @Operation(summary = "Pagamentos por período", description = "Lista pagamentos em um intervalo de datas")
    ResponseEntity<List<PaymentDTO>> findByDateRange(
            @Parameter(description = "Data inicial") LocalDateTime startDate,
            @Parameter(description = "Data final") LocalDateTime endDate);

    // === Relatórios Financeiros ===

    @Operation(summary = "Receita total", description = "Calcula receita total no período")
    ResponseEntity<Double> getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate);

    @Operation(summary = "Receita do dia", description = "Retorna a receita total do dia atual")
    ResponseEntity<Double> getTodayRevenue();

    @Operation(summary = "Receita do mês", description = "Retorna a receita total do mês atual")
    ResponseEntity<Double> getMonthRevenue();

    @Operation(summary = "Receita diária", description = "Retorna receita agrupada por dia")
    ResponseEntity<List<Map<String, Object>>> getDailyRevenue(LocalDateTime startDate, LocalDateTime endDate);

    @Operation(summary = "Receita do barbeiro", description = "Calcula receita de um barbeiro no período")
    ResponseEntity<Double> getBarberRevenue(Long barberId, LocalDateTime startDate, LocalDateTime endDate);

    @Operation(summary = "Comissão do barbeiro", description = "Calcula comissão de um barbeiro no período")
    ResponseEntity<Double> getBarberCommission(Long barberId, LocalDateTime startDate, LocalDateTime endDate);

    @Operation(summary = "Ticket médio", description = "Calcula valor médio dos pagamentos no período")
    ResponseEntity<Double> getAverageTicket(LocalDateTime startDate, LocalDateTime endDate);

    @Operation(summary = "Receita por método", description = "Retorna receita agrupada por método de pagamento")
    ResponseEntity<List<Map<String, Object>>> getRevenueByMethod(LocalDateTime startDate, LocalDateTime endDate);

    // === Estatísticas ===

    @Operation(summary = "Total de pagamentos", description = "Conta pagamentos no período")
    ResponseEntity<Long> getTotalPayments(LocalDateTime startDate, LocalDateTime endDate);

    @Operation(summary = "Pagamentos pendentes", description = "Lista pagamentos aguardando confirmação")
    ResponseEntity<List<PaymentDTO>> getPendingPayments();

    @Operation(summary = "Quantidade de pendentes", description = "Conta pagamentos pendentes")
    ResponseEntity<Long> getPendingPaymentsCount();
}
