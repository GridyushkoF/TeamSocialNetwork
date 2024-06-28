package ru.skillbox.authentication.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.security.Key;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    private final Algorithm algorithm;

    private final Key secret;

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

    public String getUserName(String token) {
        return (String) getAllClaimsFromToken(token).get("sub");
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
    }

    public boolean validate(String authToken) {
        try {
            getAllClaimsFromToken(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Подписи не совпадают. " + e.getMessage() );
        } catch (MalformedJwtException e) {
            log.error("Невалидный токен. " + e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Истекло время действия токена. " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Неподдерживаемый токен. " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Claims пустой. " + e.getMessage());
        }
        return false;
    }
}
