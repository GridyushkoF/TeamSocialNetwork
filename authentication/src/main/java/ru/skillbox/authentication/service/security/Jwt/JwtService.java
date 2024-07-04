package ru.skillbox.authentication.service.security.Jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.skillbox.authentication.service.security.AppUserDetails;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.Duration;
import java.util.Date;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    private final Algorithm algorithm;

    @Value("${security.jwt.tokenExpiration}")
    private Duration tokenExpiration;

    public String generateJwtToken(AppUserDetails user) {
        return JWT.create()
                .withIssuer("http://skillbox.ru")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + tokenExpiration.toMillis()))
                .withSubject(user.getEmail())
                .withClaim("id", user.getId())
                .sign(algorithm);
    }
}
