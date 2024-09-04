package ru.skillbox.userservice.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.skillbox.commonlib.exception.AdminAccessException;
import ru.skillbox.userservice.exception.BadRequestException;
import ru.skillbox.userservice.exception.NoSuchAccountException;
import ru.skillbox.userservice.exception.NotAuthException;
import ru.skillbox.commonlib.dto.ErrorDetail;

import java.time.LocalDateTime;

@ControllerAdvice
public class UserExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDetail> handleBadRequest(BadRequestException exception) {
        return buildResponseEntity(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAuthException.class)
    public ResponseEntity<ErrorDetail> handleUnauthorized(NotAuthException exception) {
        return buildResponseEntity(exception, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorDetail> handleNoSuchAccountException(NoSuchAccountException exception) {
        return buildResponseEntity(exception,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorDetail> handleAdminAccessException(AdminAccessException exception) {
        return buildResponseEntity(exception,HttpStatus.UNAUTHORIZED);
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
