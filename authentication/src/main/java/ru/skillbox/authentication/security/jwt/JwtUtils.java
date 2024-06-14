package ru.skillbox.authentication.security.jwt;


import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.skillbox.authentication.security.AppUserDetails;


import java.time.Duration;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.tokenExpiration}")
    private Duration tokenExpiration;

    public String generateJwtToken(AppUserDetails userDetails) {
        return generateJwtTokenFromUsername(userDetails.getUsername());
    }

    public String generateJwtTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + tokenExpiration.toMillis()))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUserName(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJwt(token)
                .getBody().getSubject();
    }

    public boolean validate(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJwt(authToken);
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
