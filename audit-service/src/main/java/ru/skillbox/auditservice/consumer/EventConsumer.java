package ru.skillbox.auditservice.consumer;


import ru.skillbox.commonlib.event.Event;

public interface EventConsumer<T extends Event> {
    void consumeEvent(T event);
}