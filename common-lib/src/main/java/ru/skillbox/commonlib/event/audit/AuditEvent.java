package ru.skillbox.commonlib.event.audit;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import ru.skillbox.commonlib.event.Event;

import java.time.ZonedDateTime;

@Data
@Builder
@ToString
public class AuditEvent implements Event {
    private Long id;
    private String entityName;
    private ActionType actionType;
    private Long userId;
    private ZonedDateTime createdAt;
}
