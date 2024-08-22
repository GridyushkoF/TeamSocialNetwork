package ru.skillbox.authentication.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseBody {
    private String message;
}
