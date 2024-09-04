package ru.skillbox.notificationservice.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skillbox.commonlib.dto.ErrorDetail;
import ru.skillbox.notificationservice.exception.SettingsAlreadyExistsException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(SettingsAlreadyExistsException.class)
    public ResponseEntity<ErrorDetail> handleBadRequest(SettingsAlreadyExistsException exception) {
        return buildResponseEntity(exception);
    }

    private ResponseEntity<ErrorDetail> buildResponseEntity(Exception exception) {
        ErrorDetail errorDetail = new ErrorDetail()
                .setTitle(exception.getMessage())
                .setStatus(HttpStatus.BAD_REQUEST.value())
                .setTimeStamp(LocalDateTime.now())
                .setDetail(exception.getMessage())
                .setDeveloperMessage(exception.getClass().getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetail);
    }
}
