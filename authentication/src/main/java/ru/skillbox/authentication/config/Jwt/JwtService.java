package ru.skillbox.authentication.config.Jwt;

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
    private String secret;

    @Value("${security.jwt.tokenExpiration}")
    private Duration tokenExpiration;


    //    public String generateToken(UserDetails userDetails){
//
//        Date issuedAt = new Date(System.currentTimeMillis());
//        Date expiration = new Date(issuedAt.getTime() + (30 * 60 * 1000));
//
//        return Jwts.builder()
////                .claims(extraClaims)
//                .subject(userDetails.getUsername())
//                .issuedAt(issuedAt)
//                .expiration(expiration)
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }
//
//
//    private Key generateKey(){
//        byte[] secreateAsBytes = Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(secreateAsBytes);
//    }
//
//    public String extractEmail(String jwt) {
//       return extractAllClaims(jwt).getSubject();
//    }
//
//    public Claims extractAllClaims(String jwt){
//        return Jwts.parser().verifyWith((SecretKey) generateKey()).build()
//                .parseSignedClaims(jwt).getPayload();
//    }
//
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
