package ru.skillbox.authentication.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.authentication.entity.User;
import ru.skillbox.authentication.repository.PasswordResetTokenRepository;
import ru.skillbox.authentication.authentication.AuthenticationRequest;
import ru.skillbox.authentication.service.PasswordResetService;

@RestController
@RequestMapping("/api/v1/auth/password")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    private final  PasswordResetTokenRepository passwordResetTokenRepository;


    /*@PostMapping("/recovery")
    public void resetPassword(@RequestParam("email") String email){
        passwordResetService.sendPasswordResetToken(email);
    }*/
    @PostMapping("/recovery")
    public void resetPassword(@RequestBody AuthenticationRequest request){
        passwordResetService.sendPasswordResetToken(request.getEmail());
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
