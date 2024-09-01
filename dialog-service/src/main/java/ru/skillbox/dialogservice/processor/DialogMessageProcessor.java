package ru.skillbox.dialogservice.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;
import ru.skillbox.commonlib.event.notification.NotificationEvent;
import ru.skillbox.commonlib.event.notification.NotificationType;

@Component
@Log4j2
@RequiredArgsConstructor
public class DialogMessageProcessor {

    private final Sinks.Many<NotificationEvent> sink;

    public void process(Long authUserId, Long userId, String content) {
        NotificationEvent event = NotificationEvent.builder()
                .authorId(authUserId)
                .userId(userId)
                .notificationType(NotificationType.MESSAGE)
                .content("'" + content + "'")
                .build();
        log.info("Message sent to notification service {}", event);
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
