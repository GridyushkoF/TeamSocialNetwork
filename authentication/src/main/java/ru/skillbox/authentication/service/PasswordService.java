package ru.skillbox.authentication.service;

import ru.skillbox.authentication.model.dto.RecoveryPasswordRequest;
import ru.skillbox.authentication.model.dto.SetPasswordRequest;
import ru.skillbox.authentication.model.dto.SimpleResponse;


public interface PasswordService {
    SimpleResponse sendToEmail(RecoveryPasswordRequest request);

    SimpleResponse setNewPassword(String recoveryLink, SetPasswordRequest request);
}
