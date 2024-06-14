package ru.skillbox.postservice.dto.pages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sort {
    private boolean empty;
    private boolean unsorted;
    private boolean sorted;
}
