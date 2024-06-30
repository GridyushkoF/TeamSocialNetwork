package ru.skillbox.authentication.security.Jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.skillbox.authentication.security.AppUserDetails;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    private final Algorithm algorithm;

    private final Duration tokenExpiration = Duration.ofMinutes(30);

    public String generateJwtToken(AppUserDetails user) {
        return JWT.create()
                .withIssuer("http://skillbox.ru")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + tokenExpiration.toMillis()))
                .withSubject(user.getEmail())
                .withClaim("id", user.getId())
                .sign(algorithm);
    }
    public String encodedSecret(String secret) {
        return Base64.getEncoder().encodeToString(secret.getBytes());
    }
}
