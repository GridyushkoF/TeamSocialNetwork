package ru.skillbox.gateway.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    public String getUserId(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJwt(token)
                .getBody().get("id").toString();
    }

    public boolean validate(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
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
