package ru.skillbox.userservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.skillbox.userservice.exceptions.BadRequestException;
import ru.skillbox.userservice.exceptions.NotAuthException;
import ru.skillbox.userservice.model.entity.ErrorDetail;

import java.util.Date;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionController extends ResponseEntityExceptionHandler {


    @ExceptionHandler
    public ResponseEntity<?> badRequest(BadRequestException exception) {
        return templateResponseException(exception, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    public ResponseEntity<?> unAuthorized(NotAuthException exception) {
        return templateResponseException(exception, HttpStatus.UNAUTHORIZED);
    }


    private ResponseEntity<?> templateResponseException(String message, Exception exception, HttpStatus status) {
        return ResponseEntity.status(status.value())
                .body(createDefaultErrorDetail(message, exception, status.value()));
    }

    private ResponseEntity<?> templateResponseException(Exception exception, HttpStatus status) {
        return templateResponseException(exception.getMessage(), exception, status);
    }

    private ErrorDetail createDefaultErrorDetail(String message, Exception exception, int statusValue) {
        return new ErrorDetail()
                .setTitle(message)
                .setStatus(statusValue)
                .setTimeStamp(new Date().getTime())
                .setDetail(exception.getMessage())
                .setDeveloperMessage(exception.getClass().getName());
    }

}
