package ru.skillbox.authentication.controller;


import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.authentication.model.web.AuthenticationResponse;
import ru.skillbox.authentication.service.CaptchaService;
import ru.skillbox.authentication.model.dto.RegUserDto;
import ru.skillbox.authentication.exception.CaptchaValidatedExcepction;
import ru.skillbox.authentication.repository.UserRepository;
import ru.skillbox.authentication.model.web.AuthenticationRequest;
import ru.skillbox.authentication.service.AuthenticationService;
import ru.skillbox.authentication.exception.AlreadyExistsException;
import ru.skillbox.authentication.service.security.AppUserDetails;
import ru.skillbox.authentication.service.security.Jwt.JwtService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final CaptchaService captchaService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final JwtService jwtService;


    @PostMapping("/register")
    public void createUser(@RequestBody RegUserDto userDto) {
        if (!captchaService.validateCaptcha(userDto.getCaptchaSecret()
                , userDto.getCaptchaCode())) {
            log.error("Пользователь '" + userDto.getEmail() + "' ввел невалидную капчу.");
            throw new CaptchaValidatedExcepction("No pass captcha");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            log.error("Ошибка регистрации. Email '" + userDto.getEmail() + "' уже зарегистрирован.");
            throw new AlreadyExistsException("Email уже занят");
        }
        authenticationService.register(userDto);

    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout")
    public void logout() {
        log.info("Пользователь вышел из системы.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(
            @RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.login(authenticationRequest));
    }
}
