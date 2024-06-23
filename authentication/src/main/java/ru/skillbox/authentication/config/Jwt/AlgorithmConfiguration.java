package ru.skillbox.authentication.config.Jwt;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("security.jwt")
@Setter
public class AlgorithmConfiguration {

    private String secret;

    @Bean
    public Algorithm algorithm(){
        return Algorithm.HMAC512(secret.getBytes());
    }
}
