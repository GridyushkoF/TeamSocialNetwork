package ru.skillbox.postservice.model.dto.pages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.skillbox.postservice.model.dto.CommentDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageCommentDto {
    private Long totalElements;
    private int totalPages;
    private int number;
    private int size;
    private List<CommentDto> content;
    private Sort sort;
    private boolean first;
    private boolean last;
    private int numberOfElements;
    private Pageable pageable;
    private boolean empty;
}
