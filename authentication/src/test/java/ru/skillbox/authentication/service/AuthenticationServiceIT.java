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
import ru.skillbox.authentication.exception.IncorrectPasswordException;
import ru.skillbox.authentication.model.dto.RegUserDto;
import ru.skillbox.authentication.model.entity.sql.User;
import ru.skillbox.authentication.model.web.AuthenticationRequest;
import ru.skillbox.authentication.model.web.AuthenticationResponse;
import ru.skillbox.authentication.repository.sql.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@AutoConfigureMockMvc
@RequiredArgsConstructor
@Testcontainers
class AuthenticationServiceIT extends TestDependenciesContainer {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Container
    private static final RedisContainer redisContainer =
            new RedisContainer(DockerImageName.parse("redis:6.2.6"))
                    .withExposedPorts(6379)
                    .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(AuthenticationServiceIT.class)));

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redisContainer::getHost);
        registry.add("spring.redis.port", redisContainer::getFirstMappedPort);
    }

    @Test
    @DisplayName("Register user - correct data, success")
    void testRegisterUser_correct_success() {
        RegUserDto userDto = new RegUserDto();
        userDto.setEmail("newuser@mail.com");
        userDto.setPassword1("password123");
        userDto.setPassword2("password123");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        assertDoesNotThrow(() -> authenticationService.register(userDto));
        User savedUser = userRepository.findByEmail("newuser@mail.com").orElseThrow();
        assertEquals("John", savedUser.getFirstName());
        assertTrue(passwordEncoder.matches("password123", savedUser.getPassword()));
    }

    @Test
    @DisplayName("Register user - email already exists, error")
    void testRegisterUser_emailAlreadyExists_error() {
        User existingUser = saveTestUserAccountInDbAndGet();
        RegUserDto userDto = new RegUserDto();
        userDto.setEmail(existingUser.getEmail());
        userDto.setPassword1("password123");
        userDto.setPassword2("password123");
        userDto.setFirstName("Jane");
        userDto.setLastName("Doe");
        assertThrows(AlreadyExistsException.class, () -> authenticationService.register(userDto));
    }

    @Test
    @DisplayName("Login user - correct data, success")
    void testLoginUser_correct_success() {
        User testUser = saveTestUserAccountInDbAndGet();
        AuthenticationRequest loginRequest = new AuthenticationRequest(testUser.getEmail(), "123");
        AuthenticationResponse response = authenticationService.login(loginRequest);
        assertNotNull(response);
        assertNotNull(response.getAccessToken());
        assertEquals(response.getAccessToken(), response.getRefreshToken());
    }

    @Test
    @DisplayName("Login user - incorrect password, error")
    void testLoginUser_incorrectPassword_error() {
        User testUser = saveTestUserAccountInDbAndGet();
        AuthenticationRequest loginRequest = new AuthenticationRequest(testUser.getEmail(), "invalidPassword");
        assertThrows(IncorrectPasswordException.class, () -> authenticationService.login(loginRequest));
    }
}