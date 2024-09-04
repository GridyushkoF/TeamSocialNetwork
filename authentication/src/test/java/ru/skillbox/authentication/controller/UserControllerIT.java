package ru.skillbox.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.testcontainers.RedisContainer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.skillbox.authentication.TestDependenciesContainer;
import ru.skillbox.authentication.model.dto.RegUserDto;
import ru.skillbox.authentication.model.entity.sql.User;
import ru.skillbox.authentication.model.security.AppUserDetails;
import ru.skillbox.authentication.model.web.AuthenticationRequest;
import ru.skillbox.authentication.model.web.AuthenticationResponse;
import ru.skillbox.authentication.repository.nosql.EmailChangeRequestRepository;
import ru.skillbox.authentication.repository.sql.UserRepository;
import ru.skillbox.authentication.service.AuthenticationService;
import ru.skillbox.authentication.service.CaptchaService;
import ru.skillbox.authentication.service.security.jwt.JwtService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@AutoConfigureMockMvc
@Testcontainers
class UserControllerIT extends TestDependenciesContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CaptchaService captchaService;

    @MockBean
    private AuthenticationService authenticationService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        reset(captchaService, authenticationService);
    }

    @Container
    private static final RedisContainer redisContainer =
            new RedisContainer(DockerImageName.parse("redis:6.2.6"))
                    .withExposedPorts(6379)
                    .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(UserControllerIT.class)));

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redisContainer::getHost);
        registry.add("spring.redis.port", redisContainer::getFirstMappedPort);
    }

    @Test
    @DisplayName("Create user - without captcha, success")
    void createUser_withoutCaptcha_success() throws Exception {
        RegUserDto userDto = getRegUserDto();
        when(captchaService.validateCaptcha(any(), any())).thenReturn(true);
        doNothing().when(authenticationService).register(any(RegUserDto.class));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        verify(captchaService, times(1)).validateCaptcha(any(), any());
        verify(authenticationService, times(1)).register(any(RegUserDto.class));
    }

    @Test
    @DisplayName("Create user - when captcha is invalid, should return bad request")
    void createUser_whenCaptchaInvalid_ShouldReturnBadRequest() throws Exception {
        RegUserDto userDto = getRegUserDto();
        when(captchaService.validateCaptcha(any(), any())).thenReturn(false);
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        verify(captchaService, times(1)).validateCaptcha(any(), any());
        verify(authenticationService, never()).register(any(RegUserDto.class));
    }

    @Test
    @DisplayName("Create user - when email busy, should return bad request")
    void createUser_emailBusy_error() throws Exception {
        when(captchaService.validateCaptcha(any(), any())).thenReturn(true);
        RegUserDto userDto = getRegUserDto();
        userRepository.save(User.builder()
                .email(userDto.getEmail())
                .build());
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        verify(captchaService, times(1)).validateCaptcha(any(), any());
        verify(authenticationService, never()).register(any(RegUserDto.class));
    }

    @Test
    @DisplayName("Logout - success")
    void logout() throws Exception {
        User user = saveTestUserAccountInDbAndGet();
        AppUserDetails appUserDetails = new AppUserDetails(user);
        String jwtToken = jwtService.generateJwtToken(appUserDetails);
        mockMvc.perform(post("/logout")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Login user - success")
    void loginUser() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("test@mail.ru", "12345");
        AuthenticationResponse response = new AuthenticationResponse("token", "refreshToken");
        when(authenticationService.login(any(AuthenticationRequest.class))).thenReturn(response);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("token"));

        verify(authenticationService, times(1)).login(any(AuthenticationRequest.class));
    }

    private static @NotNull RegUserDto getRegUserDto() {
        RegUserDto userDto = new RegUserDto();
        userDto.setEmail("test@mail.ru");
        userDto.setPassword1("12345");
        userDto.setPassword2("12345");
        userDto.setFirstName("Ivan");
        userDto.setLastName("Ivanov");
        userDto.setCaptchaCode("captchaCode");
        userDto.setCaptchaSecret("captchaSecret");
        return userDto;
    }
}