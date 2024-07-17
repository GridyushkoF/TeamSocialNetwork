package ru.skillbox.authentication.service.security;

import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;

@Configuration
@ConfigurationProperties("security.jwt")
@Setter
public class AlgorithmConfiguration {

    private String secret;
    @Bean
    public Algorithm algorithm(){
        return Algorithm.HMAC512(secret.getBytes());
    }

    @Bean
    public Key key(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

}
