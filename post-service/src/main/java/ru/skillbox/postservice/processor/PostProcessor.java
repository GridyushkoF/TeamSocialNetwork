package ru.skillbox.postservice.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;
import ru.skillbox.commondto.notification.NotificationEvent;
import ru.skillbox.commondto.notification.NotificationType;
import ru.skillbox.postservice.model.entity.Post;

@Component
@Log4j2
public class PostProcessor {

    private final Sinks.Many<NotificationEvent> sink;

    @Autowired
    public PostProcessor(Sinks.Many<NotificationEvent> sink) {
        this.sink = sink;
    }

    public void process(Post post) {
        NotificationEvent event = NotificationEvent.builder()
                .id(post.getId())
                .authorId(post.getAuthorId())
                .notificationType(NotificationType.POST)
                .build();
        log.info("Message sent to notification service {}", event);
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
