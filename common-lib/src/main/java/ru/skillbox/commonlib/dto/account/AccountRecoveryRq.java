package ru.skillbox.commonlib.dto.account;

import lombok.Data;

@Data
public class AccountRecoveryRq {
    String email;
    String password;
}
