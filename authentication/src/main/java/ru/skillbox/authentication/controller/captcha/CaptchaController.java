package ru.skillbox.authentication.controller.captcha;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.authentication.service.CaptchaService;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaService captchaService;

    @GetMapping("/captcha")
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
