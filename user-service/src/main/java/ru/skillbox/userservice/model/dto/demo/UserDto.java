package ru.skillbox.userservice.model.dto.demo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {
    private Long userId;
    private String username;
    private String fullName;
    private String email;
}
