package ru.skillbox.authentication.model.web;

import lombok.Data;

@Data
public class SetPasswordRequest {
    private String temp;
    private String password;
}
