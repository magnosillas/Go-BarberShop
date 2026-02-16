package br.edu.ufape.gobarber.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de resposta paginada genérica.
 * Segue o padrão da arquitetura base.
 * 
 * @author GoBarber Team
 * @version 2.0
 * @param <T> Tipo do conteúdo da página
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {

    private Long totalElements;
    private Integer totalPages;
    private Integer page;
    private Integer size;
    private List<T> content;

    /**
     * Construtor estático para criar página vazia.
     * 
     * @param page Número da página
     * @param size Tamanho da página
     * @param <T> Tipo do conteúdo
     * @return Página vazia
     */
    public static <T> PageResponse<T> empty(int page, int size) {
        return new PageResponse<>(0L, 0, page, size, List.of());
    }
}
