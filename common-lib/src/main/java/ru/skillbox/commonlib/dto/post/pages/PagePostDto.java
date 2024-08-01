package ru.skillbox.commonlib.dto.post.pages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.skillbox.commonlib.dto.post.PostDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagePostDto {
    private Long totalElements;
    private int totalPages;
    private int number;
    private int size;
    private List<PostDto> content;
    private Sort sort;
    private boolean first;
    private boolean last;
    private int numberOfElements;
    private Pageable pageable;
    private boolean empty;

}
