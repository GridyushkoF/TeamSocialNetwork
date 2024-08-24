package ru.skillbox.commonlib.dto.account;

import lombok.Data;

@Data
public class AccountRecoveryRequest {
    String email;
    String password;
}
