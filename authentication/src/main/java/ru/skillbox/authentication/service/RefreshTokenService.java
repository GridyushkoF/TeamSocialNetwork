//package ru.skillbox.authentication.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import ru.kirill.entity.RefreshToken;
//import ru.kirill.exception.RefreshTokenException;
//import ru.kirill.repository.RefreshTokenRepository;
//
//import java.time.Duration;
//import java.time.Instant;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class RefreshTokenService {
//
//    @Value("${app.jwt.refreshToken}")
//    private Duration refreshTokenExp;
//
//    private final RefreshTokenRepository refreshTokenRepository;
//
//    public Optional<RefreshToken> findByRefreshToken(String token) {
//        return refreshTokenRepository.findByToken(token);
//    }
//
//    public RefreshToken createRefreshToken(Long userId) {
//        RefreshToken refreshToken = RefreshToken.builder()
//                .userId(userId)
//                .expiryDate(Instant.now().plusMillis(refreshTokenExp.toMillis()))
//                .token(UUID.randomUUID().toString())
//                .build();
//        refreshToken = refreshTokenRepository.save(refreshToken);
//
//        return refreshToken;
//    }
//
//    public RefreshToken checkRefreshToken(RefreshToken token) {
//        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
//            refreshTokenRepository.delete(token);
//            throw new RefreshTokenException(token.getToken(), "Истекло время действия токена");
//        }
//        return token;
//    }
//
//    public void deleteByUserId(Long userId) {
//        refreshTokenRepository.deleteByUserId(userId);
//    }
//}
