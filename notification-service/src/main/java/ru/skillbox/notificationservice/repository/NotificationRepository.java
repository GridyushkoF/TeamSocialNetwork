package ru.skillbox.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.notificationservice.util.settings.NotificationSetting;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationSetting, Long> {
    Optional<NotificationSetting> findByUserId(Long Id);
}
