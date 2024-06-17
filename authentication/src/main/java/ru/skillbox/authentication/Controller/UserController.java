package ru.skillbox.authentication.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.authentication.DTO.UserDTO;
import ru.skillbox.authentication.authentication.AuthenticationRequest;
import ru.skillbox.authentication.authentication.AuthenticationService;
import ru.skillbox.authentication.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;

    @Autowired
    public UserController(UserService userService,PasswordEncoder passwordEncoder, AuthenticationService authenticationService ){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public void createUser(@RequestBody UserDTO user) {
        if (user.getPassword1().equals(user.getPassword2())){
            userService.createUser(user);
        }

    }

    @PostMapping("/login")
    public void loginUser(@RequestBody AuthenticationRequest request){
        authenticationService.login(request);
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
