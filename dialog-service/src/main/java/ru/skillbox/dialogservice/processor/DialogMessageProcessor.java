package ru.skillbox.dialogservice.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;
import ru.skillbox.commonlib.notification.NotificationEvent;
import ru.skillbox.commonlib.notification.NotificationType;

@Component
@Log4j2
public class DialogMessageProcessor {

    private final Sinks.Many<NotificationEvent> sink;

    @Autowired
    public DialogMessageProcessor(Sinks.Many<NotificationEvent> sink) {
        this.sink = sink;
    }

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
