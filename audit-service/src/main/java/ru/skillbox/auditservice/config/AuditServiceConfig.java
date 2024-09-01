package ru.skillbox.auditservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.auditservice.consumer.EventConsumer;
import ru.skillbox.commonlib.event.audit.AuditEvent;

import java.util.function.Consumer;

@Configuration
public class AuditServiceConfig {
    private final EventConsumer<AuditEvent> auditEventEventConsumer;

    @Autowired
    public AuditServiceConfig(
            EventConsumer<AuditEvent> auditEventEventConsumer) {
        this.auditEventEventConsumer = auditEventEventConsumer;
    }

    @Bean
    public Consumer<AuditEvent> auditEventSubscriber() {
        return auditEventEventConsumer::consumeEvent;
    }
}
