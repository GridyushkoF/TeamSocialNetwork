package ru.skillbox.postservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import ru.skillbox.commondto.notification.NotificationEvent;

import java.util.function.Supplier;

@Configuration
public class PostServiceConfig {

    @Bean
    public Sinks.Many<NotificationEvent> sink() {
        return Sinks.many()
                .multicast()
                .directBestEffort();
    }
    @Bean
    public Supplier<Flux<NotificationEvent>> postEventPublisher(
            Sinks.Many<NotificationEvent> publisher) {
        return publisher::asFlux;
    }
}
