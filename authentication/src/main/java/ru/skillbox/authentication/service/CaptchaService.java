package ru.skillbox.authentication.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.skillbox.authentication.controller.captcha.Captcha;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@Getter
public class CaptchaService {

    private final Cage cage = new GCage();
    private final ConcurrentHashMap<String , Captcha> hashMap = new ConcurrentHashMap<>();

    public String generateCaptcha() {
        String text = cage.getTokenGenerator().next();

        String tokenCaptch = UUID.randomUUID().toString();
        Captcha captcha = new Captcha(text, System.currentTimeMillis());

        hashMap.put(tokenCaptch, captcha);

       return tokenCaptch;
    }

    public String generateCaptchaImage(String text) throws IOException {

        BufferedImage image = cage.drawImage(text);
        ByteArrayOutputStream bite = new ByteArrayOutputStream();
        ImageIO.write(image, "png" , bite);


        return Base64.getEncoder().encodeToString(bite.toByteArray());
    }

    public boolean validateCaptcha(String token, String text){
        Captcha ex1 = hashMap.get(token);

        return ex1 != null && ex1.getText().equals(text);
    }

    @Scheduled(fixedRate = 60 * 1000)
    public void CleanUpCaptcha(){
        long expirationTime = TimeUnit.MINUTES.toMillis(5);
        long currentTime = System.currentTimeMillis();
        hashMap.entrySet().removeIf(entry ->
                currentTime - entry.getValue().getTimestamp() > expirationTime);
    }

}
