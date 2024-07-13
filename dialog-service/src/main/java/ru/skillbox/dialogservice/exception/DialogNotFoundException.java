package ru.skillbox.dialogservice.exception;

import java.text.MessageFormat;

public class DialogNotFoundException extends RuntimeException {
    public DialogNotFoundException (Long member1Id,Long member2Id) {
        super(MessageFormat.format("dialog with members" +
                " with ids {0} and {1} not exists)",member1Id,member2Id));
    }
}
