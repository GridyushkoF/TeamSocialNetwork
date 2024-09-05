package ru.skillbox.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.skillbox.authentication.TestDependenciesContainer;
import ru.skillbox.authentication.model.entity.nosql.EmailChangeRequest;
import ru.skillbox.authentication.model.entity.sql.User;
import ru.skillbox.authentication.model.web.ChangeEmailRequest;
import ru.skillbox.authentication.model.web.ChangeEmailRequestWrapper;
import ru.skillbox.authentication.model.web.ChangePasswordRequest;
import ru.skillbox.authentication.repository.nosql.EmailChangeRequestRepository;
import ru.skillbox.authentication.repository.sql.UserRepository;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@AutoConfigureMockMvc
@Testcontainers
class UserSecurityControllerIT extends TestDependenciesContainer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EmailChangeRequestRepository emailChangeRequestRepository;

    @Autowired
    private MockMvc mockMvc;

    private User user;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        user = saveTestUserAccountInDbAndGet();
    }

    @Container
    private static final RedisContainer redisContainer =
            new RedisContainer(DockerImageName.parse("redis:6.2.6"))
                    .withExposedPorts(6379)
                    .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(UserSecurityControllerIT.class)));

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);
    }

    @Test
    @DisplayName("Change password - correct data, success")
    void testChangePassword_correct_success() throws Exception {
        testChangePassword("123").andExpect(status().isOk());
    }

    @Test
    @DisplayName("Change password - old password wrong, no access")
    void testChangePassword_oldPasswordWrong_noAccess() throws Exception {
        testChangePassword("WRONGPASSWORD").andExpect(status().isBadRequest());
    }

    private ResultActions testChangePassword(String userOldPassword) throws Exception {
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                .oldPassword(userOldPassword)
                .newPassword1("newPassword123")
                .newPassword2("newPassword123")
                .build();
        return mockMvc.perform(post("/change-password-link")
                .header("id", user.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(changePasswordRequest)));
    }

    @Test
    @DisplayName("Send change email request - correct data, success")
    void testSendChangeEmailRequest_correct_success() throws Exception {
        ChangeEmailRequest changeEmailRequest = ChangeEmailRequest.builder()
                .email(ChangeEmailRequestWrapper.builder().email("newmail@gmail.com").build())
                .build();
        mockMvc.perform(post("/change-email-link")
                        .header("id", user.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(changeEmailRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("created request to change email"));
    }

    @Test
    @DisplayName("Accept email changing - valid key, success")
    void testAcceptEmailChanging_validKey_success() throws Exception {
        String userEmail = user.getEmail();
        String changeEmailKey = "validChangeEmailKey";
        emailChangeRequestRepository.save(
                EmailChangeRequest.builder()
                        .id(UUID.randomUUID().toString())
                        .oldEmail(user.getEmail())
                        .newEmail("testnewmail@google.ru")
                        .currentTempCode(changeEmailKey)
                        .build()
        );
        mockMvc.perform(get("/change-email/verification/{userEmail}/{changeEmailKey}/confirm", userEmail, changeEmailKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("changed successful"));
    }

    @Test
    @DisplayName("Accept email changing - invalid key, no access")
    void testAcceptEmailChanging_invalidKey_noAccess() throws Exception {
        String userEmail = user.getEmail();
        String changeEmailKey = "invalidKey";
        mockMvc.perform(get("/change-email/verification/{userEmail}/{changeEmailKey}/confirm", userEmail, changeEmailKey))
                .andExpect(status().isNotFound());
    }
}