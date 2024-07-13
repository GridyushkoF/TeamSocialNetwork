package ru.skillbox.commondto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContentableDto {
    private List<?> content;
}
