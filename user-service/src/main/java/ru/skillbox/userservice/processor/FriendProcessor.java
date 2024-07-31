package ru.skillbox.userservice.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;
import ru.skillbox.commondto.notification.NotificationEvent;
import ru.skillbox.commondto.notification.NotificationType;

@Component
@Log4j2
public class FriendProcessor {

    private final Sinks.Many<NotificationEvent> sink;

    @Autowired
    public FriendProcessor(Sinks.Many<NotificationEvent> sink) {
        this.sink = sink;
    }

    public void process(Long authUserId, Long userId, NotificationType type) {
        NotificationEvent event = NotificationEvent.builder()
                .authorId(authUserId)
                .userId(userId)
                .notificationType(type)
                .content(NotificationType.FRIEND_REQUEST.equals(type) ? "Заявка в друзья" : "День рождения друга")
                .build();
        log.info("Message sent to notification service {}", event);
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
