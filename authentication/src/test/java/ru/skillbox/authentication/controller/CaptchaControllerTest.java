package ru.skillbox.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.authentication.model.dto.Captcha;
import ru.skillbox.authentication.service.CaptchaService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CaptchaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CaptchaService captchaService;

    private String token;
    private String text;
    private String image;

    @BeforeEach
    public void setUp() throws IOException {
        token = UUID.randomUUID().toString();
        text = "captchaText";

        BufferedImage mockImage = new BufferedImage(100, 50, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream bite = new ByteArrayOutputStream();
        ImageIO.write(mockImage, "png", bite);
        image = Base64.getEncoder().encodeToString(bite.toByteArray());

        when(captchaService.generateCaptcha()).thenReturn(token);
        when(captchaService.generateCaptchaImage(text)).thenReturn(image);

        Captcha captcha = new Captcha(text, System.currentTimeMillis());
        ConcurrentHashMap<String, Captcha> captchaMap = new ConcurrentHashMap<>();
        captchaMap.put(token, captcha);

        when(captchaService.getHashMap()).thenReturn(captchaMap);
    }

    @Test
    public void getCaptcha_ShouldReturnCaptchaData() throws Exception {
        String expectedResponse = String.format(
                "{\"secret\":\"%s\",\"image\":\"data:image/png;base64,%s\"}",
                token,
                image
        );

        mockMvc.perform(get("/captcha")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse));
    }
}