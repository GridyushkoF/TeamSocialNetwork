package ru.skillbox.authentication.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.authentication.entity.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}
