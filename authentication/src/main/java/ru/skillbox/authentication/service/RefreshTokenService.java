package ru.skillbox.authentication.service;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.skillbox.authentication.exception.RefreshTokenException;
import ru.skillbox.authentication.model.entity.RefreshToken;
import ru.skillbox.authentication.repository.RefreshTokenRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    @Value("${security.jwt.tokenExpiration}")
    private Duration tokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        var refreshToken = RefreshToken.builder()
                .userId(userId)
                .expiryDate(Instant.now().plusMillis(tokenExpiration.toMillis()))
                .token(UUID.randomUUID().toString())
                .build();
        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public RefreshToken checkRefreshToken(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) > 0) {
            refreshTokenRepository.delete(token);
            log.error("Refresh токен не валиден");
            throw new RefreshTokenException(token.getToken(), "Refresh токен не валиден");
        }
        return token;
    }

    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }


}
