package ru.skillbox.authentication.config.Jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.skillbox.authentication.model.AppUserDetails;

import java.time.Duration;
import java.util.Date;

@Component
@Slf4j
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secretKey;

    @Value("${security.jwt.tokenExpiration}")
    private Duration tokenExpiration;

    private final Algorithm algorithm;

    public JwtService(Algorithm algorithm) {
        this.algorithm = algorithm;
    }


    public String generateJwtToken(AppUserDetails userDetails) {
        return generateJwtTokenFromUsernameAndId(userDetails.getUsername(), userDetails.getId());
    }

    public String generateJwtTokenFromUsernameAndId(String username, long id) {
        return JWT.create()
                .withSubject(username)
                .withClaim("id", id)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + tokenExpiration.toMillis()))
                .sign(algorithm);
    }



    public String getUserName(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJwt(token)
                .getBody().getSubject();
    }

    public boolean validate(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
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
