package ru.skillbox.authentication.model.dto;

import lombok.Data;

@Data
public class SetPasswordRequest {
    private String temp;
    private String password;
}
