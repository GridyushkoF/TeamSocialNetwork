package ru.skillbox.authentication.model.web;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeEmailRequest {
    private ChangeEmailRequestWrapper email;
}
