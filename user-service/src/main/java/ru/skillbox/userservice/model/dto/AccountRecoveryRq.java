package ru.skillbox.userservice.model.dto;

import lombok.Data;

@Data
public class AccountRecoveryRq {
    String email;
    String password;
}
