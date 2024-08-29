package ru.skillbox.postservice.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;
import ru.skillbox.commonlib.event.audit.ActionType;
import ru.skillbox.commonlib.event.audit.AuditEvent;
import ru.skillbox.commonlib.event.notification.NotificationEvent;
import ru.skillbox.commonlib.event.notification.NotificationType;
import ru.skillbox.postservice.model.entity.Post;

import java.time.ZonedDateTime;

@Component
@Log4j2
public class PostProcessor {

    private final Sinks.Many<NotificationEvent> postSink;
    private final Sinks.Many<AuditEvent> auditSink;

    @Autowired
    public PostProcessor(Sinks.Many<NotificationEvent> postSink, Sinks.Many<AuditEvent> auditSink) {
        this.postSink = postSink;
        this.auditSink = auditSink;
    }

    public void notificationProcess(Post post) {
        NotificationEvent event = NotificationEvent.builder()
                .id(post.getId())
                .authorId(post.getAuthorId())
                .notificationType(NotificationType.POST)
                .build();
        log.info("Message sent to notification service {}", event);
        postSink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    public void auditProcess(Post post, ActionType actionType) {
        AuditEvent event = AuditEvent.builder()
                .id(post.getId())
                .entityName("POST")
                .userId(post.getAuthorId())
                .actionType(actionType)
                .createdAt(ZonedDateTime.now())
                .build();
        log.info("Message sent to audit service {}", event);
        auditSink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

}
