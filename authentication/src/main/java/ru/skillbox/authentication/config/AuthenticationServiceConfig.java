package ru.skillbox.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import ru.skillbox.commonlib.event.audit.AuditEvent;

import java.util.function.Supplier;

@Configuration
public class AuthenticationServiceConfig {

    @Bean
    public Sinks.Many<AuditEvent> auditSink() {
        return Sinks.many()
                .multicast()
                .directBestEffort();
    }
    @Bean
    public Supplier<Flux<AuditEvent>> auditEventPublisher(
            Sinks.Many<AuditEvent> publisher) {
        return publisher::asFlux;
    }
}
