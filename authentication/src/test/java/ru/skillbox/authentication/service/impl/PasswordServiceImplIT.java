package ru.skillbox.authentication.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skillbox.authentication.TestDependenciesContainer;
import ru.skillbox.authentication.exception.EntityNotFoundException;
import ru.skillbox.authentication.exception.IncorrectRecoveryLinkException;
import ru.skillbox.authentication.model.dto.RecoveryPasswordRequest;
import ru.skillbox.authentication.model.dto.SetPasswordRequest;
import ru.skillbox.authentication.model.dto.SimpleResponse;
import ru.skillbox.authentication.model.entity.sql.User;
import ru.skillbox.authentication.repository.sql.UserRepository;
import ru.skillbox.authentication.util.CryptoUtil;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Testcontainers
class PasswordServiceImplIT extends TestDependenciesContainer {

    @Autowired
    private PasswordServiceImpl passwordServiceImpl;

    @Autowired
    private CryptoUtil cryptoUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Send to email - existing user, email sent")
    void testSendToEmail_existingUser_emailSent() {
        User testUser = saveTestUserAccountInDbAndGet();
        RecoveryPasswordRequest request = new RecoveryPasswordRequest();
        request.setEmail(testUser.getEmail());
        request.setTemp(System.currentTimeMillis());

        SimpleResponse response = passwordServiceImpl.sendToEmail(request);
        assertEquals("Ссылка для воостановления пароля успешно отправлена на Email", response.getMessage());
    }

    @Test
    @DisplayName("Send to email - non-existent user, throws EntityNotFoundException")
    void testSendToEmail_nonExistentUser_throwsEntityNotFoundException() {
        RecoveryPasswordRequest request = new RecoveryPasswordRequest();
        request.setEmail("nonexistent@mail.com");
        request.setTemp(System.currentTimeMillis());

        assertThrows(EntityNotFoundException.class, () -> passwordServiceImpl.sendToEmail(request));
    }

    @Test
    @DisplayName("Set new password - correct recovery link, password updated")
    void testSetNewPassword_correctRecoveryLink_passwordUpdated() {
        User testUser = saveTestUserAccountInDbAndGet();
        String recoveryLink = cryptoUtil.encodeWithTemp(System.currentTimeMillis(), testUser.getId());

        SetPasswordRequest request = new SetPasswordRequest();
        request.setPassword("newPassword123");

        SimpleResponse response = passwordServiceImpl.setNewPassword(recoveryLink, request);
        assertEquals("Пароль успешно изменён", response.getMessage());

        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertTrue(passwordEncoder.matches("newPassword123", updatedUser.getPassword()));
    }

    @Test
    @DisplayName("Set new password - incorrect recovery link, throws IncorrectRecoveryLinkException")
    void testSetNewPassword_incorrectRecoveryLink_throwsIncorrectRecoveryLinkException() {
        String incorrectLink = "incorrect-link";

        SetPasswordRequest request = new SetPasswordRequest();
        request.setPassword("newPassword123");

        assertThrows(IncorrectRecoveryLinkException.class, () -> passwordServiceImpl.setNewPassword(incorrectLink, request));
    }
}