package ru.skillbox.authentication.dto;

import lombok.Data;

@Data
public class SetPasswordRequest {
    private String temp;
    private String password;
}
