package ru.skillbox.authentication.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.authentication.Entity.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}
