package ru.skillbox.authentication.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.authentication.authentication.AuthenticationResponse;
import ru.skillbox.authentication.dto.RegUserDto;
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
    public void createUser(@RequestBody RegUserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new AlreadyExistsException("Email уже занят");
        }
        authenticationService.register(userDto);



//        if (user.getPassword1().equals(user.getPassword2())){
//            userService.createUser(user);
//        }

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.login(request));
    }


    /*@PostMapping("/register")
    public void createUser(@RequestParam("email") String email, @RequestParam("password") String password,
                           @RequestParam("firstName") String firstName,
                           @RequestParam("secondName") String secondName){
        Users user = new Users();
        user.setFirstName(firstName);
        user.setSecondName(secondName);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        userService.createUser(user);
    }*/
}
