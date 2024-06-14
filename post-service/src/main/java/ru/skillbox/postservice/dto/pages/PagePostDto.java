package ru.skillbox.postservice.dto.pages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.postservice.dto.PostDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagePostDto {
    private Long totalElements;
    private Long totalPages;
    private Long number;
    private Long size;
    private List<PostDto> content;
    private Sort sort;
    private boolean first;
    private boolean last;
    private Long numberOfElements;
    private PageableObject pageable;
    private boolean empty;

}
