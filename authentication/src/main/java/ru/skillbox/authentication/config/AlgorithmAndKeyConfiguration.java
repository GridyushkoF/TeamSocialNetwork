package ru.skillbox.authentication.config;

import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.util.Base64;

@Configuration
@ConfigurationProperties("security.jwt")
@Setter
public class AlgorithmAndKeyConfiguration {

    private String secret;
    @Bean
    public Algorithm algorithm(){
        return Algorithm.HMAC512(secret.getBytes());
    }

    @Bean
    public Key key(){
        return Keys.hmacShaKeyFor(Base64.getEncoder().encodeToString(secret.getBytes()).getBytes());
    }

}
