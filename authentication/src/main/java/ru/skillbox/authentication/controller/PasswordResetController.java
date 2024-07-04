package ru.skillbox.authentication.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.authentication.model.dto.RecoveryPasswordRequest;
import ru.skillbox.authentication.model.dto.SetPasswordRequest;
import ru.skillbox.authentication.model.dto.SimpleResponse;
import ru.skillbox.authentication.model.entity.User;
import ru.skillbox.authentication.exception.EntityNotFoundException;
import ru.skillbox.authentication.repository.PasswordResetTokenRepository;
import ru.skillbox.authentication.repository.UserRepository;
import ru.skillbox.authentication.service.PasswordService;
import ru.skillbox.authentication.service.PasswordResetService;

@RestController
@RequestMapping("/password")
@RequiredArgsConstructor
@Slf4j
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordService passwordService;
    private final UserRepository userRepository;

    @PostMapping("/recovery")
    public ResponseEntity<SimpleResponse> resetPassword(
            @RequestBody RecoveryPasswordRequest request) {
        if (!userRepository.existsByEmail(request.getEmail())) {
            log.error("Восстановление по емаил: " + request.getEmail()
                    + " не удалось. Email не найден в БД");
            throw new EntityNotFoundException("Пользователь с данным Email не зарегистрирован");
        }
        return ResponseEntity.ok(passwordService.sendToEmail(request));
    }

    @PostMapping("/recovery/{recoveryLink}")
    public ResponseEntity<SimpleResponse> resetPasswordFromMessage(
            @PathVariable String recoveryLink,
            @RequestBody SetPasswordRequest request) {
        return ResponseEntity.ok(passwordService.setNewPassword(recoveryLink, request));
    }

    @GetMapping("/resetPassword")
    public String displayResetPassword(@RequestParam("token") String token){
        if (!passwordResetService.isValidPasswordResetToken(token))
            //отобразить страницу смены пароля
            return "Invalid or expired token";
        return "Token is valid";
    }

    // кнопка "сменить пароль"
    @PostMapping("/savePassword")
    public String changePassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword){
        if (!passwordResetService.isValidPasswordResetToken(token)){
            return "Invalid or expired token";
        }
        User user = passwordResetTokenRepository.findByToken(token).get().getUser();
        passwordResetService.changePassword(user, newPassword);
        return "Password successfully reset";
    }



}
