package ru.skillbox.authentication.config.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.skillbox.authentication.Entity.Users;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${security.jwt.expiration-minutes}")
    private long EXPIRATION_MINUTES;

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;


    public String generateToken(Users users, Map<String, Object> extraClaims){

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(issuedAt.getTime() + (30 * 60 * 1000));


        return Jwts.builder()
                .claims(extraClaims)
                .subject(users.getFirstName())
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private Key generateKey(){

        byte[] secreateAsBytes = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(secreateAsBytes);
    }

    public String extractEmail(String jwt) {

       return extractAllClaims(jwt).getSubject();


    }

    public Claims extractAllClaims(String jwt){
        return Jwts.parser().verifyWith((SecretKey) generateKey()).build()
                .parseSignedClaims(jwt).getPayload();
    }
}
