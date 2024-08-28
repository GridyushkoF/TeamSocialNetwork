package ru.skillbox.authentication.service;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.authentication.exception.AlreadyExistsException;
import ru.skillbox.authentication.exception.EntityNotFoundException;
import ru.skillbox.authentication.exception.IncorrectPasswordException;
import ru.skillbox.authentication.model.dto.SimpleResponse;
import ru.skillbox.authentication.model.entity.nosql.EmailChangeRequest;
import ru.skillbox.authentication.model.entity.sql.User;
import ru.skillbox.authentication.model.web.ChangeEmailRequest;
import ru.skillbox.authentication.model.web.ChangePasswordRequest;
import ru.skillbox.authentication.repository.nosql.EmailChangeRequestRepository;
import ru.skillbox.authentication.repository.sql.UserRepository;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserSecurityDataService {
    private final UserRepository userRepository;
    private final EmailChangeRequestRepository emailChangeRequestRepository;
    private final MailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private static final int SLASH_NUMBER = 47;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${app.changeEmailHost}")
    private String changeEmailHost;

    @Transactional
    public SimpleResponse sendEmailChangeRequestToEmail(ChangeEmailRequest changeEmailRequest, Long userId) throws NoSuchAlgorithmException {
        if (userRepository.existsByEmail(changeEmailRequest.getEmail().getEmail())) {
            throw new AlreadyExistsException("this email already exists in database");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("user not found"));
        emailChangeRequestRepository.findByOldEmail(user.getEmail()).ifPresent(emailChangeRequestRepository::delete);
        EmailChangeRequest emailChangeRequest = emailChangeRequestRepository.save(EmailChangeRequest.builder()
                .id(UUID.randomUUID().toString())
                .currentTempCode(generateSecureTempKey(150))
                .oldEmail(user.getEmail())
                .newEmail(changeEmailRequest.getEmail().getEmail())
                .build());
        sendToEmail(user.getEmail(),emailChangeRequest);
        return new SimpleResponse("created request to change email");
    }

    public void sendToEmail(String oldEmail,EmailChangeRequest emailChangeRequest) {
        String subject = "Смена пароля";
        String messageBody = getMailBody(oldEmail,emailChangeRequest);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromEmail);
        mailMessage.setTo(oldEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageBody);
        mailSender.send(mailMessage);
        log.info("Заявка на смену пароля создана и отправлена на почту");
    }

    private String getMailBody(String oldEmail, EmailChangeRequest emailChangeRequest) {
        var userOpt = userRepository.findByEmail(oldEmail);
        if (userOpt.isPresent()) {
            return  "Для смены email: " + changeEmailHost + "/api/v1/auth/change-email/verification/" +
                    emailChangeRequest.getOldEmail() + "/" + emailChangeRequest.getCurrentTempCode() + "/confirm";
        }
        log.error("Смена по емаил: {} не удалась. Email не найден в БД", emailChangeRequest.getOldEmail());
        throw new EntityNotFoundException("Пользователь с данным Email не зарегистрирован");
    }
    public String generateSecureTempKey(int length) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
        byte[] keyBytes = new byte[length];
        secureRandom.nextBytes(keyBytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : keyBytes) {
            if (b != SLASH_NUMBER) {
                sb.append(String.format("%02x", b));
            }
        }
        return sb.toString();
    }
    @Transactional
    public void changeEmail(String userEmail, String changeEmailKey) {
        EmailChangeRequest emailChangeRequest = emailChangeRequestRepository.findByOldEmail(userEmail).orElseThrow(() -> new EntityNotFoundException("user with email " + userEmail + "not found"));
        if(!emailChangeRequest.getCurrentTempCode().equals(changeEmailKey)) {
            throw new IncorrectPasswordException("provided key not found");
        }
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        user.setEmail(emailChangeRequest.getNewEmail());
        userRepository.save(user);
    }
    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordRequest, Long currentAuthUserId) {
        User user = userRepository.findById(currentAuthUserId).orElseThrow();
        if(!changePasswordRequest.getNewPassword1().equals(changePasswordRequest.getNewPassword2())) {
            throw new IncorrectPasswordException("passwords not match");
        }
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(),user.getPassword())) {
            throw new IncorrectPasswordException("old password not matches password in database");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword2()));
        userRepository.save(user);
    }
}
