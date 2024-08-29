package ru.skillbox.authentication.model.web;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword1;
    private String newPassword2;
}
