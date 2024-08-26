package ru.skillbox.dialogservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import ru.skillbox.commonlib.event.notification.NotificationEvent;

import java.util.function.Supplier;

@Configuration
public class DialogServiceConfig {

    @Bean
    public Sinks.Many<NotificationEvent> sink() {
        return Sinks.many()
                .multicast()
                .directBestEffort();
    }
    @Bean
    public Supplier<Flux<NotificationEvent>> dialogMessageEventPublisher(
            Sinks.Many<NotificationEvent> publisher) {
        return publisher::asFlux;
    }
}
