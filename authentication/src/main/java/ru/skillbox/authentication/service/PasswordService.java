package ru.skillbox.authentication.service;

import ru.skillbox.authentication.dto.RecoveryPasswordRequest;
import ru.skillbox.authentication.dto.SetPasswordRequest;
import ru.skillbox.authentication.dto.SimpleResponse;


public interface PasswordService {
    SimpleResponse sendToEmail(RecoveryPasswordRequest request);

    SimpleResponse setNewPassword(String recoveryLink, SetPasswordRequest request);
}
