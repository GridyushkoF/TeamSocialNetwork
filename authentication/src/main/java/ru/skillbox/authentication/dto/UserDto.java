package ru.skillbox.authentication.dto;

import lombok.Data;

@Data
public class UserDto {

    private String email;
    private String password1;
    private String password2;
    private String firstName;
    private String lastName;
    private String code;
    private String token;
}
