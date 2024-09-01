package ru.skillbox.authentication.repository.sql;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.authentication.model.entity.sql.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}
