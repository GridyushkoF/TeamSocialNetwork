package ru.skillbox.commondto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageCommentDto {
    private Long totalElements;
    private Long totalPages;
    private Long number;
    private Long size;
    private List<CommentDto> content;
    private Sort sort;
    private boolean first;
    private boolean last;
    private boolean numberOfElements;
    private PageableObject pageable;
    private boolean empty;
}
