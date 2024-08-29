package ru.skillbox.notificationservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.commonlib.event.notification.NotificationEvent;
import ru.skillbox.notificationservice.consumer.EventConsumer;

import java.util.function.Consumer;

@Configuration
public class NotificationServiceConfig {
    private final EventConsumer<NotificationEvent> notificationEventEventConsumer;

    @Autowired
    public NotificationServiceConfig(
            EventConsumer<NotificationEvent> notificationEventEventConsumer) {
        this.notificationEventEventConsumer = notificationEventEventConsumer;
    }

    @Bean
    public Consumer<NotificationEvent> notificationEventSubscriber() {
        return notificationEventEventConsumer::consumeEvent;
    }
}
