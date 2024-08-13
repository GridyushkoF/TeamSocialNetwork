package ru.skillbox.authentication.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.authentication.service.impl.PasswordServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class PasswordServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordServiceImpl passwordService;

    @Test
    public void testSendToEmail() throws Exception {
        String email = "test@example.com";

        mockMvc.perform(post("/api/password/recovery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Email sent"));
    }

    @Test
    public void testSetNewPassword() throws Exception {
        String recoveryLink = "http://example.com/recovery";
        String newPassword = "newPassword123";

        mockMvc.perform(post("/api/password/set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"" + newPassword + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password updated"));
    }
}