package ru.skillbox.userservice.model.dto.demo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserSearchResponseDto {
    private List<?> content;
}
