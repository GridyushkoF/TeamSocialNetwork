package ru.skillbox.postservice.dto.pages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageableObject {
    private Long offset;
    private Sort sort;
    private Long pageSize;
    private Long pageNumber;
    private boolean paged;
    private boolean unPaged;
}
