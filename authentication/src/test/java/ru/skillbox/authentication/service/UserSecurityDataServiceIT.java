package ru.skillbox.authentication.service;

import com.redis.testcontainers.RedisContainer;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.skillbox.authentication.TestDependenciesContainer;
import ru.skillbox.authentication.exception.AlreadyExistsException;
import ru.skillbox.authentication.exception.EntityNotFoundException;
import ru.skillbox.authentication.model.entity.sql.User;
import ru.skillbox.authentication.model.web.ChangeEmailRequest;
import ru.skillbox.authentication.model.web.ChangeEmailRequestWrapper;
import ru.skillbox.authentication.model.web.ChangePasswordRequest;
import ru.skillbox.authentication.repository.nosql.EmailChangeRequestRepository;
import ru.skillbox.authentication.repository.sql.UserRepository;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Testcontainers
class UserSecurityDataServiceIT extends TestDependenciesContainer {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailChangeRequestRepository emailChangeRequestRepository;
    @Autowired
    private UserSecurityDataService userSecurityDataService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Container
    private static final RedisContainer redisContainer =
            new RedisContainer(DockerImageName.parse("redis:6.2.6"))
                    .withExposedPorts(6379)
                    .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(UserSecurityDataServiceIT.class)));

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redisContainer::getHost);
        registry.add("spring.redis.port", redisContainer::getFirstMappedPort);
    }

//    @Test
//    @DisplayName("Send email change request to email - success")
//    void testSendEmailChangeRequestToEmail_correct_success() throws NoSuchAlgorithmException {
//        User testUser = saveTestUserAccountInDbAndGet();
//        ChangeEmailRequest changeEmailRequest = ChangeEmailRequest.builder()
//                .email(new ChangeEmailRequestWrapper("newmail@gmail.com")).build();
//        userSecurityDataService.sendEmailChangeRequestToEmail(changeEmailRequest, testUser.getId());
//        assertEquals(1, emailChangeRequestRepository.count());
//    }

    @Test
    @DisplayName("Send email change request to email - user not exists")
    void testSendEmailChangeRequestToEmail_userNotExists_error() {
        ChangeEmailRequest changeEmailRequest = ChangeEmailRequest.builder()
                .email(new ChangeEmailRequestWrapper("randomMail@mail.ru")).build();

        assertThrows(EntityNotFoundException.class, () ->
                userSecurityDataService.sendEmailChangeRequestToEmail(changeEmailRequest, 210L));
        assertEquals(0, emailChangeRequestRepository.count());
    }

    @Test
    @DisplayName("Send email change request to email - email already registered")
    void testSendEmailChangeRequestToEmail_mailAlreadyRegistered_error() {
        User user = saveTestUserAccountInDbAndGet();
        ChangeEmailRequest changeEmailRequest = ChangeEmailRequest.builder()
                .email(new ChangeEmailRequestWrapper(user.getEmail())).build();

        assertThrows(AlreadyExistsException.class, () ->
                userSecurityDataService.sendEmailChangeRequestToEmail(changeEmailRequest, 210L));
        assertEquals(0, emailChangeRequestRepository.count());
    }

//    @Test
//    @DisplayName("Send to email - success")
//    void testSendToEmail() {
//        User user = saveTestUserAccountInDbAndGet();
//        EmailChangeRequest emailChangeRequest = EmailChangeRequest.builder()
//                .oldEmail(user.getEmail())
//                .newEmail("new@mail.ru")
//                .currentTempCode("tempCode")
//                .build();
//        assertDoesNotThrow(() -> {
//            userSecurityDataService.sendToEmail(user.getEmail(), emailChangeRequest);
//        });
//    }

    @Test
    @DisplayName("Generate secure temp key - success")
    void testGenerateSecureTempKey() throws NoSuchAlgorithmException {
        int length = 150;
        String tempKey = userSecurityDataService.generateSecureTempKey(length);
        assertEquals(length * 2, tempKey.length());
    }

//    @Test
//    @DisplayName("Change email - success")
//    void testChangeEmail() {
//        User testUser = saveTestUserAccountInDbAndGet();
//        ChangeEmailRequest changeEmailRequest = ChangeEmailRequest.builder()
//                .email(new ChangeEmailRequestWrapper("newmail@gmail.com")).build();
//
//        assertDoesNotThrow(() -> {
//            userSecurityDataService.sendEmailChangeRequestToEmail(changeEmailRequest, testUser.getId());
//        });
//
//        EmailChangeRequest emailChangeRequest = emailChangeRequestRepository.findByOldEmail(testUser.getEmail()).orElseThrow();
//        assertDoesNotThrow(() -> {
//            userSecurityDataService.changeEmail(testUser.getEmail(), emailChangeRequest.getCurrentTempCode());
//        });
//        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
//        assertEquals("newmail@gmail.com", updatedUser.getEmail());
//    }

    @Test
    @DisplayName("Change password - success")
    void testChangePassword() {
        User testUser = saveTestUserAccountInDbAndGet();
        String oldPassword = "123";
        String newPassword = "newPassword123";
        testUser.setPassword(passwordEncoder.encode(oldPassword));
        userRepository.save(testUser);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setOldPassword(oldPassword);
        changePasswordRequest.setNewPassword1(newPassword);
        changePasswordRequest.setNewPassword2(newPassword);
        assertDoesNotThrow(() -> userSecurityDataService.changePassword(changePasswordRequest, testUser.getId()));
        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertTrue(passwordEncoder.matches(newPassword, updatedUser.getPassword()));
    }
}