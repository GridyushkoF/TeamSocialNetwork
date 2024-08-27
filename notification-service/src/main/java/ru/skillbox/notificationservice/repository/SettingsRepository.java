package ru.skillbox.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.notificationservice.model.entity.Settings;

import java.util.Optional;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {
    Optional<Settings> findByUserId(Long id);
}
