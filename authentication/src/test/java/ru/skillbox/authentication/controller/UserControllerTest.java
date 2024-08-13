package ru.skillbox.authentication.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.skillbox.authentication.model.entity.Role;
//import ru.skillbox.authentication.model.entity.User;
//import ru.skillbox.authentication.repository.UserRepository;
//import ru.skillbox.authentication.service.CaptchaService;
//
//
//import java.util.Optional;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.refEq;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ActiveProfiles("test")
//@SpringBootTest
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
//    private User newUser;
//
//    @BeforeEach
//    public void setUp() {
//        Mockito.when(passwordEncoder.encode(anyString()))
//                .thenAnswer(invocation -> invocation.getArgument(0) + "_some_fake_encoding");
//
//        newUser = User.builder()
//                .firstName("Ivan")
//                .lastName("Ivanov")
//                .email("ivanov@users.com")
//                .password("superpas99")
//                .role(Role.USER)
//                .build();
//    }
//
//    @Test
//    public void createUser() throws Exception {
//        Mockito.when(captchaService.validateCaptcha("token", "secret")).thenReturn(true);
//        Mockito.when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
//        Mockito.when(userRepository.save(refEq(newUser))).thenReturn(newUser);
//
//        mvc.perform(
//                        post("/auth/register")
//                                .accept(MediaType.APPLICATION_JSON)
//                                .content("{\"firstName\":\"Ivan\",\"lastName\":\"Ivanov\"," +
//                                        "\"password1\":\"superpas99\",\"password2\":\"superpas99\"," +
//                                        "\"email\":\"ivanov@users.com\",\"captchaToken\":\"token\",\"captchaSecret\":\"secret\"}")
//                                .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(content().string(containsString(newUser.getEmail())));
//    }
//}