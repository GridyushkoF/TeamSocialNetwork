package ru.skillbox.authentication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.authentication.service.CaptchaService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Captcha Controller", description = "Captcha API")
public class CaptchaController {

    private final CaptchaService captchaService;

    @GetMapping("/captcha")
    @Operation(summary = "Get captcha")
    public ResponseEntity<Map<String , String>> getCaptcha() throws IOException {
        String token = captchaService.generateCaptcha();
        String text = captchaService.getHashMap().get(token).getText();
        String image = captchaService.generateCaptchaImage(text);

        Map<String , String> response = new HashMap<>();

        response.put("secret" , token);
        response.put("image" , "data:image/png;base64," + image);

        return ResponseEntity.ok(response);
    }
}
