package ru.skillbox.authentication.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {

    private String token;
}
