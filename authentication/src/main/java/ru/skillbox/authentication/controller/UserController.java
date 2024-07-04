package ru.skillbox.authentication.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.authentication.model.web.AuthenticationResponse;
import ru.skillbox.authentication.service.CaptchaService;
import ru.skillbox.authentication.model.dto.RegUserDto;
import ru.skillbox.authentication.exception.CaptchaValidatedExcepction;
import ru.skillbox.authentication.repository.UserRepository;
import ru.skillbox.authentication.model.web.AuthenticationRequest;
import ru.skillbox.authentication.service.AuthenticationService;
import ru.skillbox.authentication.exception.AlreadyExistsException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final CaptchaService captchaService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;


    @PostMapping("/register")
    public void createUser(@RequestBody RegUserDto userDto) {
        if (!captchaService.validateCaptcha(userDto.getCaptchaSecret()
                , userDto.getCaptchaCode())){
            throw new CaptchaValidatedExcepction("No pass captcha");
        }


        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new AlreadyExistsException("Email уже занят");
        }
        authenticationService.register(userDto);

    }

    @ResponseStatus(HttpStatus.OK)
    public void logout() {
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(authenticationService.login(authenticationRequest));
    }
}
