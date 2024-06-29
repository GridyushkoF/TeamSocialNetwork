package ru.skillbox.authentication.captcha;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class CaptchaController {

    private CaptchaService captchaService;

    @GetMapping("/captcha")
    public ResponseEntity<?> getCaptcha() throws IOException {
        String token = captchaService.generateCaptcha();
        String text = captchaService.getHashMap().get(token).getText();
        String image = captchaService.generateCaptchaImage(text);

        Map<String , String> response = new HashMap<>();

        response.put("token" , token);
        response.put("image" , "data:image/png;base64," + image);

        return ResponseEntity.ok(response);
    }
}
