package br.edu.ufape.gobarber.doc;

import br.edu.ufape.gobarber.dto.client.ClientCreateDTO;
import br.edu.ufape.gobarber.dto.client.ClientDTO;
import br.edu.ufape.gobarber.dto.page.PageDTO;
import br.edu.ufape.gobarber.exceptions.DataBaseException;
import br.edu.ufape.gobarber.exceptions.NotFoundException;
import br.edu.ufape.gobarber.model.Client;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Clientes", description = "API para gerenciamento de clientes e programa de fidelidade")
public interface ClientControllerDoc {

    @Operation(summary = "Criar cliente", description = "Cria um novo cliente com foto opcional")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
                    content = @Content(schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Email ou CPF já cadastrado")
    })
    ResponseEntity<ClientDTO> create(ClientCreateDTO dto, MultipartFile photo) throws DataBaseException;

    @Operation(summary = "Atualizar cliente", description = "Atualiza dados de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "409", description = "Email ou CPF já em uso")
    })
    ResponseEntity<ClientDTO> update(
            @Parameter(description = "ID do cliente") Long id,
            ClientCreateDTO dto, MultipartFile photo) throws NotFoundException, DataBaseException;

    @Operation(summary = "Excluir cliente", description = "Remove um cliente do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    ResponseEntity<Void> delete(@Parameter(description = "ID do cliente") Long id) throws NotFoundException, DataBaseException;

    @Operation(summary = "Buscar cliente por ID", description = "Retorna os dados de um cliente específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    ResponseEntity<ClientDTO> findById(@Parameter(description = "ID do cliente") Long id) throws NotFoundException;

    @Operation(summary = "Listar clientes", description = "Retorna lista paginada de todos os clientes")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    ResponseEntity<PageDTO<ClientDTO>> findAll(Pageable pageable);

    @Operation(summary = "Buscar cliente por email", description = "Busca um cliente pelo endereço de email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    ResponseEntity<ClientDTO> findByEmail(@Parameter(description = "Email do cliente") String email) throws NotFoundException;

    @Operation(summary = "Buscar cliente por telefone", description = "Busca um cliente pelo número de telefone")
    ResponseEntity<ClientDTO> findByPhone(@Parameter(description = "Telefone do cliente") String phone) throws NotFoundException;

    @Operation(summary = "Buscar cliente por CPF", description = "Busca um cliente pelo CPF")
    ResponseEntity<ClientDTO> findByCpf(@Parameter(description = "CPF do cliente") String cpf) throws NotFoundException;

    @Operation(summary = "Pesquisar clientes", description = "Pesquisa clientes pelo nome")
    ResponseEntity<List<ClientDTO>> search(@Parameter(description = "Termo de pesquisa") String name);

    @Operation(summary = "Obter foto do cliente", description = "Retorna a foto de perfil do cliente")
    ResponseEntity<byte[]> getPhoto(@Parameter(description = "ID do cliente") Long id) throws NotFoundException;

    @Operation(summary = "Atualizar foto", description = "Atualiza a foto de perfil do cliente")
    ResponseEntity<Void> updatePhoto(Long id, MultipartFile photo) throws NotFoundException, DataBaseException;

    @Operation(summary = "Remover foto", description = "Remove a foto de perfil do cliente")
    ResponseEntity<Void> deletePhoto(Long id) throws NotFoundException;

    // === Programa de Fidelidade ===

    @Operation(summary = "Adicionar pontos de fidelidade", description = "Adiciona pontos ao programa de fidelidade do cliente")
    @ApiResponse(responseCode = "200", description = "Pontos adicionados com sucesso")
    ResponseEntity<ClientDTO> addLoyaltyPoints(
            @Parameter(description = "ID do cliente") Long id,
            @Parameter(description = "Quantidade de pontos") int points,
            @Parameter(description = "Motivo da adição") String reason) throws NotFoundException;

    @Operation(summary = "Resgatar pontos", description = "Resgata pontos do programa de fidelidade")
    ResponseEntity<ClientDTO> redeemLoyaltyPoints(Long id, int points) throws NotFoundException;

    @Operation(summary = "Registrar visita", description = "Registra uma visita do cliente e atualiza métricas")
    ResponseEntity<ClientDTO> registerVisit(Long id, double amount) throws NotFoundException;

    @Operation(summary = "Obter desconto de fidelidade", description = "Retorna o percentual de desconto baseado no tier")
    ResponseEntity<Double> getLoyaltyDiscount(Long id) throws NotFoundException;

    @Operation(summary = "Listar por tier de fidelidade", description = "Lista clientes de um tier específico")
    ResponseEntity<List<ClientDTO>> getByLoyaltyTier(
            @Parameter(description = "Tier de fidelidade") Client.LoyaltyTier tier);

    // === Marketing ===

    @Operation(summary = "Aniversariantes do dia", description = "Lista clientes que fazem aniversário hoje")
    ResponseEntity<List<ClientDTO>> getBirthdayClientsToday();

    @Operation(summary = "Aniversariantes do mês", description = "Lista clientes que fazem aniversário no mês")
    ResponseEntity<List<ClientDTO>> getBirthdayClientsMonth(Integer month);

    @Operation(summary = "Clientes inativos", description = "Lista clientes sem agendamentos recentes")
    ResponseEntity<List<ClientDTO>> getInactiveClients(int days);

    @Operation(summary = "Top clientes", description = "Lista os clientes que mais gastaram")
    ResponseEntity<List<ClientDTO>> getTopSpenders(int limit);

    @Operation(summary = "Clientes para promoções", description = "Lista clientes que aceitam receber promoções")
    ResponseEntity<List<ClientDTO>> getClientsForPromotions();

    // === Estatísticas ===

    @Operation(summary = "Total de clientes", description = "Retorna o número total de clientes")
    ResponseEntity<Long> getTotalClients();

    @Operation(summary = "Clientes ativos", description = "Retorna o número de clientes ativos")
    ResponseEntity<Long> getActiveClients();

    @Operation(summary = "Distribuição por tier", description = "Retorna distribuição de clientes por tier de fidelidade")
    ResponseEntity<?> getLoyaltyDistribution();

    @Operation(summary = "Definir barbeiro preferido", description = "Define o barbeiro preferido do cliente")
    ResponseEntity<ClientDTO> setPreferredBarber(Long id, Long barberId) throws NotFoundException;

    @Operation(summary = "Clientes por barbeiro preferido", description = "Lista clientes que preferem um barbeiro")
    ResponseEntity<List<ClientDTO>> getClientsByPreferredBarber(Long barberId);
}
