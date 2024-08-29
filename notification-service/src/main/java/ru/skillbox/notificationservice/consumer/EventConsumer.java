package ru.skillbox.notificationservice.consumer;


import ru.skillbox.commonlib.event.Event;

public interface EventConsumer<T extends Event> {
    void consumeEvent(T event);
}