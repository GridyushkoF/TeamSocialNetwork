package ru.skillbox.dialogservice.handler.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skillbox.dialogservice.exception.DialogNotFoundException;
import ru.skillbox.dialogservice.model.dto.SetStatusMessageReadDto;
import ru.skillbox.dialogservice.model.dto.SetStatusMessageReadRs;

@ControllerAdvice
public class DialogExceptionHandler {
    @ExceptionHandler(DialogNotFoundException.class)
    public SetStatusMessageReadRs handleDialogNotFoundException(DialogNotFoundException e) {
        return new SetStatusMessageReadRs(
                e.getMessage(),
                System.currentTimeMillis(),
                new SetStatusMessageReadDto("Dialog Not Found!"),
                e.getCause().toString());
    }
}
