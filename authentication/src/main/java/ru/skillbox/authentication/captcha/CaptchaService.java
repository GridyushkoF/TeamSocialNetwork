package ru.skillbox.authentication.captcha;

import com.github.cage.Cage;
import com.github.cage.GCage;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
    private ConcurrentHashMap<String , Captcha> hashMap = new ConcurrentHashMap<>();

    public String generateCaptcha(){

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
        long expereshionTime = TimeUnit.MINUTES.toMillis(5);
        long currentTime = System.currentTimeMillis();
        hashMap.entrySet().removeIf(entry ->
                currentTime - entry.getValue().getTimestamp() > expereshionTime);
    }

}
