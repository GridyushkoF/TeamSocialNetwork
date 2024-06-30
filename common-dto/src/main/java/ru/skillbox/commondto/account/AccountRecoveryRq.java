package ru.skillbox.commondto.account;

import lombok.Data;

@Data
public class AccountRecoveryRq {
    String email;
    String password;
}
