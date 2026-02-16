package br.edu.ufape.gobarber.dto.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * DTO genérico para paginação de resultados.
 * @param <T> Tipo do conteúdo da página
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean empty;

    /**
     * Cria um PageDTO a partir de um Page do Spring Data.
     */
    public static <T> PageDTO<T> fromPage(Page<T> page) {
        return PageDTO.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();
    }

    /**
     * Cria um PageDTO vazio.
     */
    public static <T> PageDTO<T> empty() {
        return PageDTO.<T>builder()
                .content(List.of())
                .page(0)
                .size(0)
                .totalElements(0)
                .totalPages(0)
                .first(true)
                .last(true)
                .empty(true)
                .build();
    }
}
