package ru.skillbox.commondto.dto.account;

import lombok.Data;

@Data
public class AccountRecoveryRq {
    String email;
    String password;
}
