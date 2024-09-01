package ru.skillbox.authentication.service.security.jwt;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.skillbox.authentication.model.entity.sql.User;
import ru.skillbox.authentication.model.security.AppUserDetails;

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

    public String generateJwtToken(AppUserDetails userDetails) {
        return JWT.create()
                .withIssuer("http://skillbox.ru")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + tokenExpiration.toMillis()))
                .withSubject(userDetails.getEmail())
                .withClaim("authorities", userDetails.getAuthorities().stream().toList().toString())
                .withClaim("id", userDetails.getId())
                .sign(algorithm);
    }


    public String generateJwtTokenFromUser(User user) {
        return JWT.create()
                .withIssuer("http://skillbox.ru")
                .withSubject(user.getEmail())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + tokenExpiration.toMillis()))
                .withClaim("authorities", user.getRole().toString())
                .withClaim("id", user.getId())
                .sign(algorithm);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getEmail(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("http://skillbox.ru")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }
}
