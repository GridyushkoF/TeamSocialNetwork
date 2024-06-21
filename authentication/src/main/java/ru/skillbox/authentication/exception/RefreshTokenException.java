package ru.skillbox.authentication.exception;

public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException(String token, String message) {
        super("Попытка обновить токен: " + token + " не удалась. " + message);
    }

    public RefreshTokenException(String message) {
        super(message);
    }
}
