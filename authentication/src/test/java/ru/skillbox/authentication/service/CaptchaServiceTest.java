package ru.skillbox.authentication.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skillbox.authentication.model.dto.Captcha;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CaptchaServiceTest {

    @Autowired
    private CaptchaService captchaService;

    @Test
    void testGenerateCaptcha() {
        String token = captchaService.generateCaptcha();
        assertNotNull(token);
        assertTrue(captchaService.getHashMap().containsKey(token));
    }

    @Test
    void testGenerateCaptchaImage() throws IOException {
        String text = "test";
        String imageBase64 = captchaService.generateCaptchaImage(text);
        assertNotNull(imageBase64);
        assertFalse(imageBase64.isEmpty());
    }

    @Test
    void testValidateCaptcha_Success() {
        String token = captchaService.generateCaptcha();
        Captcha captcha = captchaService.getHashMap().get(token);

        assertNotNull(captcha);

        boolean isValid = captchaService.validateCaptcha(token, captcha.getText());
        assertTrue(isValid);
    }

    @Test
    void testValidateCaptcha_Failure() {
        String token = UUID.randomUUID().toString();
        String wrongText = "wrongText";

        boolean isValid = captchaService.validateCaptcha(token, wrongText);
        assertFalse(isValid);
    }

    @Test
    void testCleanUpCaptcha() throws InterruptedException {
        String token = captchaService.generateCaptcha();

        //Thread.sleep(TimeUnit.MINUTES.toMillis(6));

        captchaService.CleanUpCaptcha();

        assertNull(captchaService.getHashMap().get(token));
    }
}