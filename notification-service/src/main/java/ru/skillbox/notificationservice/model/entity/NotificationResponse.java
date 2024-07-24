package ru.skillbox.notificationservice.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
public class NotificationResponse {

    private LocalDateTime timeStamp;
    private ArrayList<Notification> data;

}
