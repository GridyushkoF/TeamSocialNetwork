package ru.skillbox.authentication.service;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.authentication.model.entity.PasswordResetToken;
import ru.skillbox.authentication.model.entity.User;
import ru.skillbox.authentication.repository.PasswordResetTokenRepository;
import ru.skillbox.authentication.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncode;


    public void createResetToken(User user, String token){
        PasswordResetToken userToken = new PasswordResetToken();
        userToken.setToken(token);
        userToken.setUser(user);
        userToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        passwordResetTokenRepository.save(userToken);
    }
    public void sendPasswordResetToken(String email){
        User user = userRepository.findByEmail(email).get();
        if (user == null)
            throw new IllegalArgumentException("No user with email " + email);
        String token = UUID.randomUUID().toString();
        createResetToken(user, token);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("http://localhost:4455/resetPassword?token="+ token);
        message.setFrom("testMail@mail.ru");
//        mailSender.send(message);

    }
    public boolean isValidPasswordResetToken(String token){
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token).get();
        if (passwordResetToken == null || passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now()))
            return false;
        return true;
    }

    public void changePassword(User user, String newPassword){
        user.setPassword(passwordEncode.encode(newPassword));
        userRepository.save(user);
    }

}
