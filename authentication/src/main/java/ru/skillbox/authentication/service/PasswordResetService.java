package ru.skillbox.authentication.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skillbox.authentication.Entity.PasswordResetToken;
import ru.skillbox.authentication.Entity.Users;
import ru.skillbox.authentication.Repository.PasswordResetTokenRepository;
import ru.skillbox.authentication.Repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class PasswordResetService {

    private PasswordResetTokenRepository passwordResetTokenRepository;

    private UserRepository userRepository;

    private JavaMailSender mailSender;

    private PasswordEncoder passwordEncode;

    @Autowired
    public PasswordResetService(PasswordResetTokenRepository passwordResetTokenRepository,
                                UserRepository userRepository, JavaMailSender mailSender,
                                PasswordEncoder passwordEncode){
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.passwordEncode = passwordEncode;
    }

    public void createResetToken(Users user, String token){
        PasswordResetToken userToken = new PasswordResetToken();
        userToken.setToken(token);
        userToken.setUser(user);
        userToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        passwordResetTokenRepository.save(userToken);
    }
    public void sendPasswordResetToken(String email){
        Users user = userRepository.findByEmail(email).get();
        if (user == null)
            throw new IllegalArgumentException("No user with email " + email);
        String token = UUID.randomUUID().toString();
        createResetToken(user, token);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("http://localhost:4455/resetPassword?token="+ token);
        message.setFrom("testMail@mail.ru");
        mailSender.send(message);

    }
    public boolean isValidPasswordResetToken(String token){
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token).get();
        if (passwordResetToken == null || passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now()))
            return false;
        return true;
    }

    public void changePassword(Users user, String newPassword){
        user.setPassword(passwordEncode.encode(newPassword));
        userRepository.save(user);
    }

}
