package ru.skillbox.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import ru.skillbox.authentication.TestDependenciesContainer;
import ru.skillbox.authentication.model.dto.RecoveryPasswordRequest;
import ru.skillbox.authentication.model.dto.SetPasswordRequest;
import ru.skillbox.authentication.model.entity.sql.User;
import ru.skillbox.authentication.util.CryptoUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@AutoConfigureMockMvc
class PasswordResetControllerIT extends TestDependenciesContainer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CryptoUtil cryptoUtil;

    @Test
    @DisplayName("Reset password - existing user, email sent")
    void testResetPassword() throws Exception {
        User user = saveTestUserAccountInDbAndGet();
        RecoveryPasswordRequest request = new RecoveryPasswordRequest();
        request.setEmail(user.getEmail());
        mockMvc.perform(post("/password/recovery/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Ссылка для воостановления пароля успешно отправлена на Email"));
    }

    @Test
    @DisplayName("Reset password from message - correct recovery link, password updated")
    void testResetPasswordFromMessage() throws Exception {
        User user = saveTestUserAccountInDbAndGet();
        long recoveryTemp = System.currentTimeMillis();
        String coded = cryptoUtil.encodeWithTemp(recoveryTemp, user.getId());
        SetPasswordRequest request = new SetPasswordRequest();
        request.setTemp(coded);
        request.setPassword("newPassword123");
        mockMvc.perform(post("/password/recovery/{recoveryLink}", coded)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Пароль успешно изменён"));
    }
}