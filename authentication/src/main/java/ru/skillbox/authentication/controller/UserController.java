package ru.skillbox.authentication.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.authentication.authentication.AuthenticationResponse;
import ru.skillbox.authentication.dto.UserDto;
import ru.skillbox.authentication.repository.UserRepository;
import ru.skillbox.authentication.authentication.AuthenticationRequest;
import ru.skillbox.authentication.authentication.AuthenticationService;
import ru.skillbox.authentication.exception.AlreadyExistsException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;


    @PostMapping("/register")
    public void createUser(@RequestBody UserDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new AlreadyExistsException("Email уже занят");
        }

        authenticationService.register(userDto);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.login(request));
    }

}
