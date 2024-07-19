package ru.skillbox.authentication.service.security.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    public static final String TOKEN_PREFIX = "Bearer ";

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




    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getUserName(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJwt(token)
                .getBody().getSubject();
    }


    private boolean isTokenExpired(String tokenString) {
        String jwtToken = tokenString.replace(TOKEN_PREFIX, "");
        boolean result = true;
        try {
            result = getAllClaimsFromToken(jwtToken)
                    .getExpiration()
                    .before(new Date());
        } catch (Exception ex) {
            log.error("An exception was thrown during JWT verification: {}", ex.getMessage());
        }
        return result;
    }

    public boolean isInvalid(String token) {
        return isTokenExpired(token);
    }
}
