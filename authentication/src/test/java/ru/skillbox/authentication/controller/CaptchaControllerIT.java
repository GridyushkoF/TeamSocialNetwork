package ru.skillbox.authentication.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@AutoConfigureMockMvc
class CaptchaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("check that get captcha returns correct result (image)")
    void getCaptcha_shouldReturnCaptchaData_success() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/captcha")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.secret").isNotEmpty())
                .andExpect(jsonPath("$.image").value(org.hamcrest.Matchers.startsWith("data:image/png;base64,")));
    }
}