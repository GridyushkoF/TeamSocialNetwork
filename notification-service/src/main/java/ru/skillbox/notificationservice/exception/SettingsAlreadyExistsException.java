package ru.skillbox.notificationservice.exception;

public class SettingsAlreadyExistsException extends RuntimeException {
    public SettingsAlreadyExistsException(Long userId) {
        super("Notification settings already assigned to user with id " + userId);
    }
}