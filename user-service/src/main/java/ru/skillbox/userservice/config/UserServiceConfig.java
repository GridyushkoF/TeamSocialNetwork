package ru.skillbox.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import ru.skillbox.commonlib.notification.NotificationEvent;

import java.util.function.Supplier;

@Configuration
public class UserServiceConfig {

    @Bean
    public Sinks.Many<NotificationEvent> sink() {
        return Sinks.many()
                .multicast()
                .directBestEffort();
    }
    @Bean
    public Supplier<Flux<NotificationEvent>> friendshipEventPublisher(
            Sinks.Many<NotificationEvent> publisher) {
        return publisher::asFlux;
    }
}
