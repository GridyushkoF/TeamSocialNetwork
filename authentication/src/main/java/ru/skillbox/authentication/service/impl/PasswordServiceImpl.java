package ru.skillbox.authentication.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.authentication.exception.EntityNotFoundException;
import ru.skillbox.authentication.exception.IncorrectRecoveryLinkException;
import ru.skillbox.authentication.model.dto.RecoveryPasswordRequest;
import ru.skillbox.authentication.model.dto.SetPasswordRequest;
import ru.skillbox.authentication.model.dto.SimpleResponse;
import ru.skillbox.authentication.model.entity.sql.User;
import ru.skillbox.authentication.repository.sql.UserRepository;
import ru.skillbox.authentication.service.PasswordService;
import ru.skillbox.authentication.util.CryptoUtil;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    private final JavaMailSender javaMailSender;
    private final CryptoUtil cryptoUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private long fifteenMin = 900_000;

    @Value("${spring.mail.username}")
    private String emailFrom;
    @Value("${service.recovery.uri}")
    private String activationUri;


    @Override
    public SimpleResponse sendToEmail(RecoveryPasswordRequest request) {
        String subject = "Восстановление пароля";
        String messageBody = getMailBody(request);
        String emailTo = request.getEmail();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageBody);

        javaMailSender.send(mailMessage);
        log.info("Ссылка для воостановления пароля успешно отправлена на Email " + emailTo);
        return new SimpleResponse("Ссылка для воостановления пароля успешно отправлена на Email");
    }

    private String getMailBody(RecoveryPasswordRequest request) {
        var userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent()) {
            String encodedString = cryptoUtil.encodeWithTemp(request.getTemp(), userOpt.get().getId());
            String msg  = "Для восстановления пароля перейдите по ссылке: " + activationUri;
            return msg.replace("{recoveryLink}", encodedString);
        }
        log.error("Восстановление по емаил: " + request.getEmail()
                + " не удалось. Email не найден в БД");
        throw new EntityNotFoundException("Пользователь с данным Email не зарегистрирован");
    }

    @Override
    public SimpleResponse setNewPassword(String recoveryLink, SetPasswordRequest request) {
        Map<Long, Long> decodedData = cryptoUtil.decodeWithTemp(recoveryLink);
        var entry = decodedData.entrySet().stream().findFirst();
        if (entry.isPresent()) {
            long id = entry.get().getValue();
            long temp = entry.get().getKey();
            long checkTemp = System.currentTimeMillis() - temp;
            if (checkTemp > fifteenMin) {
                log.error("Действие ссылки восстановления пароля истекло");
                throw new IncorrectRecoveryLinkException("Действие ссылки истекло");
            }
            Optional<User> userOptional = userRepository.findByIdAndIsDeletedFalse(id);
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
            log.info("Пароль успешно изменён");
            return new SimpleResponse("Пароль успешно изменён");
        } else {
            log.error("Пользователь прислал некорректную ссылку восстановления пароля");
            throw new IncorrectRecoveryLinkException("Некорректная ссылка восстановления пароля");
        }
    }
}
