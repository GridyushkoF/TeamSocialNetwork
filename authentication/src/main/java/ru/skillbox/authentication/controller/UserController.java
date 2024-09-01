package ru.skillbox.authentication.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.authentication.exception.AlreadyExistsException;
import ru.skillbox.authentication.exception.CaptchaValidatedExcepction;
import ru.skillbox.authentication.model.dto.RegUserDto;
import ru.skillbox.authentication.model.dto.SimpleResponse;
import ru.skillbox.authentication.model.web.AuthenticationRequest;
import ru.skillbox.authentication.model.web.AuthenticationResponse;
import ru.skillbox.authentication.model.web.ChangeEmailRequest;
import ru.skillbox.authentication.model.web.ChangePasswordRequest;
import ru.skillbox.authentication.repository.sql.UserRepository;
import ru.skillbox.authentication.service.AuthenticationService;
import ru.skillbox.authentication.service.CaptchaService;
import ru.skillbox.authentication.service.UserSecurityDataService;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final CaptchaService captchaService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public void createUser(@RequestBody RegUserDto userDto) {
        if (!captchaService.validateCaptcha(userDto.getCaptchaSecret()
                , userDto.getCaptchaCode())) {
            log.error("Пользователь '{}' ввел невалидную капчу.", userDto.getEmail());
            throw new CaptchaValidatedExcepction("No pass captcha");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            log.error("Ошибка регистрации. Email '{}' уже зарегистрирован.", userDto.getEmail());
            throw new AlreadyExistsException("Email уже занят");
        }
        authenticationService.register(userDto);

    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String authorizationHeader) {
        authenticationService.logout(authorizationHeader);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(
            @RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.login(authenticationRequest));
    }
}
