package ru.skillbox.notificationservice.consumer;


import ru.skillbox.commonlib.notification.Event;

public interface EventConsumer<T extends Event> {
    void consumeEvent(T event);
}