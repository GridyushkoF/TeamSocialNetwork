package ru.skillbox.authentication.service;

import ru.skillbox.authentication.model.dto.RecoveryPasswordRequest;
import ru.skillbox.authentication.model.web.SetPasswordRequest;
import ru.skillbox.authentication.model.web.SimpleResponse;


public interface PasswordService {
    SimpleResponse sendToEmail(RecoveryPasswordRequest request);

    SimpleResponse setNewPassword(String recoveryLink, SetPasswordRequest request);
}
