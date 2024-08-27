package ru.skillbox.authentication.service.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.skillbox.authentication.service.security.AppUserDetails;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.security.Key;
import java.time.Duration;
import java.util.Date;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    private final Algorithm algorithm;
    private final Key key;
    private final Duration tokenExpiration;

    public String generateJwtToken(AppUserDetails user) {
        return JWT.create()
                .withIssuer("http://skillbox.ru")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + tokenExpiration.toMillis()))
                .withSubject(user.getEmail())
                .withClaim("authorities", user.getAuthorities().stream().toList().toString())
                .withClaim("id", user.getId())
                .sign(algorithm);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
