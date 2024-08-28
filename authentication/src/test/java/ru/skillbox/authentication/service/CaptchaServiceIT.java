package ru.skillbox.authentication.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import ru.skillbox.authentication.TestDependenciesContainer;
import ru.skillbox.authentication.model.dto.Captcha;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@AutoConfigureMockMvc
class CaptchaServiceIT extends TestDependenciesContainer {

    @Autowired
    private CaptchaService captchaService;

    @Test
    @DisplayName("Generate captcha - success")
    void testGenerateCaptcha() {
        String token = captchaService.generateCaptcha();
        assertNotNull(token);
        assertTrue(captchaService.getHashMap().containsKey(token));
    }

    @Test
    @DisplayName("Generate captcha image - success")
    void testGenerateCaptchaImage() throws IOException {
        String token = captchaService.generateCaptcha();
        Captcha captcha = captchaService.getHashMap().get(token);
        String base64Image = captchaService.generateCaptchaImage(captcha.getText());
        assertNotNull(base64Image);
        assertFalse(base64Image.isEmpty());
    }

    @Test
    @DisplayName("Validate captcha - correct text returns true")
    void testValidateCaptcha() {
        String token = captchaService.generateCaptcha();
        Captcha captcha = captchaService.getHashMap().get(token);
        assertTrue(captchaService.validateCaptcha(token, captcha.getText()));
        assertFalse(captchaService.validateCaptcha(token, "wrongText"));
    }
}