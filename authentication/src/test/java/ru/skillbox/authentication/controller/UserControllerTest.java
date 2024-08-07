//package ru.skillbox.authentication.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.skillbox.authentication.model.dto.RegUserDto;
//import ru.skillbox.authentication.model.entity.Role;
//import ru.skillbox.authentication.model.entity.User;
//import ru.skillbox.authentication.repository.UserRepository;
//import ru.skillbox.authentication.service.AuthenticationService;
//import ru.skillbox.authentication.service.CaptchaService;
//import ru.skillbox.authentication.service.PasswordService;
//import ru.skillbox.authentication.config.SecurityBeanConfig;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.refEq;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ActiveProfiles("test")
//@WebMvcTest(UserController.class)
//@WithMockUser(username = "sidorovv@users.com")
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private UserRepository userRepository;
//
//    @MockBean
//    private PasswordEncoder passwordEncoder;
//
//    @MockBean
//    private CaptchaService captchaService;
//
//    @MockBean
//    private AuthenticationService authenticationService;
//
//    @Configuration
//    @ComponentScan(basePackageClasses = {UserController.class, SecurityBeanConfig.class})
//    public static class TestConf {
//    }
//
//    private User user;
//
//    private User newUser;
//
//    private RegUserDto userDto;
//
//    @BeforeEach
//    public void setUp() {
//        Mockito.when(passwordEncoder.encode(anyString()))
//                .thenAnswer(invocation -> invocation.getArgument(0) + "_some_fake_encoding");
//        user = User.builder()
//                .firstName("Petr")
//                .lastName("Petrov")
//                .email("petrov@users.com")
//                .password("superpas99")
//                .role(Role.ADMIN)
//                .build();
//        newUser = User.builder()
//                .firstName("Ivan")
//                .lastName("Ivanov")
//                .email("ivanov@users.com")
//                .password("superpas99")
//                .role(Role.USER)
//                .build();
//        userDto = new RegUserDto();
//        userDto.setEmail("ivanov@users.com");
//        userDto.setCaptchaCode("token");
//        userDto.setCaptchaSecret("secret");
//        userDto.setPassword1("superpas99");
//        userDto.setPassword2("superpas99");
//        userDto.setFirstName("Ivanov");
//        userDto.setLastName("Ivanovov");
//    }
//
//    @Test
//    public void createUser() throws Exception {
//        Mockito.when(userRepository.save(refEq(newUser))).thenReturn(newUser);
//        Mockito.when(captchaService.validateCaptcha("token", "secret")).thenReturn(true);
//        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//        Mockito.when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.ofNullable(null));
//        mvc.perform(
//                post("/auth/register")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content("{\"firstName\":\"Ivan\",\"firstName\":\"Ivanov\"," +
//                                "\"password1\":\"superpass99\",\"password2\":\"superpass99\"," +
//                                "\"email\":\"ivan@users.com\",\"captchaToken\":\"token\",\"captchaSecret\":\"secret\"}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        )
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(content().string(containsString(newUser.getEmail())));
//    }
//}
