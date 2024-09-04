package ru.skillbox.dialogservice.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.skillbox.commonlib.dto.ErrorDetail;
import ru.skillbox.dialogservice.exception.NotAuthException;
import ru.skillbox.dialogservice.exception.NotFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class DialogExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDetail> handleBadRequest(NotFoundException exception) {
        return buildResponseEntity(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAuthException.class)
    public ResponseEntity<ErrorDetail> handleUnauthorized(NotAuthException exception) {
        return buildResponseEntity(exception, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<ErrorDetail> buildResponseEntity(Exception exception, HttpStatus status) {
        var errorDetail = new ErrorDetail()
                .setTitle(exception.getMessage())
                .setStatus(status.value())
                .setTimeStamp(LocalDateTime.now())
                .setDetail(exception.getMessage())
                .setDeveloperMessage(exception.getClass().getName());
        return ResponseEntity.status(status).body(errorDetail);
    }
}
