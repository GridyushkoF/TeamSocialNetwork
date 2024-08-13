package ru.skillbox.authentication.controller;

import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.authentication.model.dto.RecoveryPasswordRequest;
import ru.skillbox.authentication.model.dto.SetPasswordRequest;
import ru.skillbox.authentication.model.dto.SimpleResponse;
import ru.skillbox.authentication.service.PasswordService;

@SpringBootTest
@AutoConfigureMockMvc
class PasswordResetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordService passwordService;

    @Test
    void testResetPassword() throws Exception {
        RecoveryPasswordRequest request = new RecoveryPasswordRequest();
        request.setEmail("test@example.com");
        SimpleResponse response = new SimpleResponse("Email sent");

        when(passwordService.sendToEmail(any(RecoveryPasswordRequest.class))).thenReturn(response);

        mockMvc.perform(post("/password/recovery/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Email sent\"}"));
    }

    @Test
    void testResetPasswordFromMessage() throws Exception {

        String recoveryLink = "http://example.com/recovery";
        SetPasswordRequest request = new SetPasswordRequest();
        request.setPassword("newPassword123");
        SimpleResponse response = new SimpleResponse("Password updated");

        when(passwordService.setNewPassword(any(String.class), any(SetPasswordRequest.class))).thenReturn(response);

        mockMvc.perform(post("/password/recovery/{recoveryLink}", recoveryLink)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"newPassword123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Password updated\"}"));
    }
}