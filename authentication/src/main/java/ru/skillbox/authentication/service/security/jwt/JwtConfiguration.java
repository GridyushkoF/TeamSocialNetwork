package ru.skillbox.authentication.service.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.time.Duration;

@Configuration
@ConfigurationProperties("security.jwt")
@Setter
public class JwtConfiguration {

    private String secret;
    private Duration tokenExpiration;

    @Bean
    public Algorithm algorithm() {
        return Algorithm.HMAC512(secret.getBytes());
    }
    @Bean
    public Key key(){

        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    @Bean
    public Duration tokenExpiration() {
        return tokenExpiration;
    }

}
