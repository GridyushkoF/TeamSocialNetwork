package ru.skillbox.userservice.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;
import ru.skillbox.commonlib.event.audit.ActionType;
import ru.skillbox.commonlib.event.audit.AuditEvent;
import ru.skillbox.userservice.model.entity.User;

import java.time.ZonedDateTime;

@Component
@Log4j2
public class AuditProcessor {

    private final Sinks.Many<AuditEvent> auditSink;

    @Autowired
    public AuditProcessor(Sinks.Many<AuditEvent> sink) {
        this.auditSink = sink;
    }

    public void process(User user, ActionType actionType, Long userId) {
        AuditEvent event = AuditEvent.builder()
                .id(user.getId())
                .entityName("USER")
                .userId(userId)
                .actionType(actionType)
                .createdAt(ZonedDateTime.now())
                .build();
        log.info("Message sent to audit service {}", event);
        auditSink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

}
