package ru.skillbox.userservice.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class ErrorDetail {

    private int status;
    private LocalDateTime timeStamp;
    private String title;
    private String detail;
    private String developerMessage;

}
