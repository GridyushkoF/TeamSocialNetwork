package ru.skillbox.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.notificationservice.model.entity.NotificationCreate;

@Repository
public interface NotificationCreateRepository extends JpaRepository<NotificationCreate, Long> {
}
