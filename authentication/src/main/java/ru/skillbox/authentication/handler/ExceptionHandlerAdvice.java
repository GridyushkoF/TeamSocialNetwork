package ru.skillbox.authentication.handler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skillbox.authentication.exception.*;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(value = RefreshTokenException.class)
    public ResponseEntity<ErrorResponseBody> refreshTokenExceptionHandler(RefreshTokenException ex) {
        return new ResponseEntity<>(new ErrorResponseBody(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    public ResponseEntity<ErrorResponseBody> alreadyExistsExceptionHandler(AlreadyExistsException ex) {
        return new ResponseEntity<>(new ErrorResponseBody(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectRecoveryLinkException.class)
    public ResponseEntity<ErrorResponseBody> incorrectRecoveryLinkExceptionHandler(
            IncorrectRecoveryLinkException ex) {
        return new ResponseEntity<>(new ErrorResponseBody(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseBody> entityNotFoundExceptionHandler(EntityNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponseBody(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = CaptchaValidatedExcepction.class)
    public ResponseEntity<ErrorResponseBody> captchaValidatedExcepction(CaptchaValidatedExcepction ex) {
        return new ResponseEntity<>(new ErrorResponseBody(ex.getMessage()), HttpStatus.valueOf(500));
    }
}
