package ru.skillbox.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.commonlib.event.notification.NotificationStatus;
import ru.skillbox.notificationservice.model.entity.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByAuthorId(long authorId);

    List<Notification> findAllByUserIdAndNotificationStatus(long authorId, NotificationStatus status);
}
