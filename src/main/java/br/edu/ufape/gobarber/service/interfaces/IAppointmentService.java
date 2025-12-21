package br.edu.ufape.gobarber.service.interfaces;

import br.edu.ufape.gobarber.controller.request.AppointmentRequest;
import br.edu.ufape.gobarber.controller.response.AppointmentResponse;
import br.edu.ufape.gobarber.controller.response.PageResponse;
import br.edu.ufape.gobarber.exceptions.DataBaseException;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface de serviço para operações de Appointment.
 * Segue o padrão da arquitetura base (Interface + Impl).
 * 
 * @author GoBarber Team
 * @version 2.0
 */
public interface IAppointmentService {

    /**
     * Cria um novo agendamento.
     * 
     * @param request Dados do agendamento
     * @return AppointmentResponse criado
     * @throws DataBaseException se houver erro no banco de dados
     */
    AppointmentResponse createAppointment(AppointmentRequest request) throws DataBaseException;

    /**
     * Atualiza um agendamento existente.
     * 
     * @param id ID do agendamento
     * @param request Novos dados do agendamento
     * @return AppointmentResponse atualizado
     * @throws DataBaseException se o agendamento não for encontrado
     */
    AppointmentResponse updateAppointment(Integer id, AppointmentRequest request) throws DataBaseException;

    /**
     * Remove um agendamento.
     * 
     * @param id ID do agendamento
     * @throws DataBaseException se o agendamento não for encontrado
     */
    void deleteAppointment(Integer id) throws DataBaseException;

    /**
     * Busca um agendamento por ID.
     * 
     * @param id ID do agendamento
     * @return AppointmentResponse encontrado
     * @throws DataBaseException se o agendamento não for encontrado
     */
    AppointmentResponse getAppointmentById(Integer id) throws DataBaseException;

    /**
     * Lista todos os agendamentos com paginação.
     * 
     * @param page Número da página
     * @param size Tamanho da página
     * @return Página de agendamentos
     */
    PageResponse<AppointmentResponse> getAllAppointments(Integer page, Integer size);

    /**
     * Lista agendamentos por barbeiro.
     * 
     * @param barberId ID do barbeiro
     * @param page Número da página
     * @param size Tamanho da página
     * @return Página de agendamentos do barbeiro
     * @throws DataBaseException se o barbeiro não for encontrado
     */
    PageResponse<AppointmentResponse> getAppointmentsByBarber(Integer barberId, Integer page, Integer size) throws DataBaseException;

    /**
     * Obtém histórico de agendamentos do barbeiro logado.
     * 
     * @param page Número da página
     * @param size Tamanho da página
     * @param request Requisição HTTP com token
     * @return Página de agendamentos do histórico
     * @throws DataBaseException se o barbeiro não for encontrado
     */
    PageResponse<AppointmentResponse> getHistoryFromToken(Integer page, Integer size, HttpServletRequest request) throws DataBaseException;

    /**
     * Lista agendamentos futuros.
     * 
     * @param page Número da página
     * @param size Tamanho da página
     * @return Página de agendamentos futuros
     */
    PageResponse<AppointmentResponse> getFutureAppointments(Integer page, Integer size);

    /**
     * Lista agendamentos futuros por barbeiro.
     * 
     * @param page Número da página
     * @param size Tamanho da página
     * @param barberId ID do barbeiro
     * @return Página de agendamentos futuros do barbeiro
     * @throws DataBaseException se o barbeiro não for encontrado
     */
    PageResponse<AppointmentResponse> getFutureAppointmentsByBarber(Integer page, Integer size, Integer barberId) throws DataBaseException;
}
