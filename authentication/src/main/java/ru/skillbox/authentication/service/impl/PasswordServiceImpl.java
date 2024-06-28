package ru.skillbox.authentication.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.authentication.model.dto.RecoveryPasswordRequest;
import ru.skillbox.authentication.model.web.SetPasswordRequest;
import ru.skillbox.authentication.model.web.SimpleResponse;
import ru.skillbox.authentication.model.entity.User;
import ru.skillbox.authentication.exception.IncorrectRecoveryLinkException;
import ru.skillbox.authentication.repository.UserRepository;
import ru.skillbox.authentication.service.PasswordService;
import ru.skillbox.authentication.utils.CryptoTool;

@Service
@Slf4j
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    private final JavaMailSender javaMailSender;
    private final CryptoTool cryptoTool;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String emailFrom;
    @Value("${service.recovery.uri}")
    private String activationUri;


    @Override
    public SimpleResponse sendToEmail(RecoveryPasswordRequest request) {
        String subject = "Восстановление пароля";
        String messageBody = getMailBody(request.getEmail());
        String emailTo = request.getEmail();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageBody);

        javaMailSender.send(mailMessage);

        return new SimpleResponse("Ссылка для воостановления пароля успешно отправлена на Email");
    }

    private String getMailBody(String email) {
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            String hashId = cryptoTool.hashOf(userOpt.get().getId());
            String msg  = "Для восстановления пароля перейдите по ссылке: " + activationUri;
            return msg.replace("{recoveryLink}", hashId);
        }
        return null;
    }

    @Override
    public SimpleResponse setNewPassword(String recoveryLink, SetPasswordRequest request) {
        Long id = cryptoTool.idOf(recoveryLink);
        if (id != null) {
        var userOpt = userRepository.findById(id);
            User user = userOpt.get();
//            TODO пока для теста не кодирую пароль. Позже добавить passwordEncoder;
            user.setPassword(request.getPassword());
            userRepository.save(user);
            return new SimpleResponse("Пароль успешно изменён");
        } else {
            throw new IncorrectRecoveryLinkException("Неверная ссылка восстановления пароля");
        }
    }
}
