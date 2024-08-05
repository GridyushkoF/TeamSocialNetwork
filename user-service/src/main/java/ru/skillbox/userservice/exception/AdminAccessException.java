package ru.skillbox.userservice.exception;

public class AdminAccessException extends RuntimeException {
    public AdminAccessException() {
        super("У вас недостаточно прав для использования этих функций, нужен уровень доступа ADMIN");
    }
}
