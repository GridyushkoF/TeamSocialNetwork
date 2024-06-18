package ru.skillbox.postservice.dto.pages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pageable {
    private Integer page;
    private Integer size;
    private List<String> sort;
}