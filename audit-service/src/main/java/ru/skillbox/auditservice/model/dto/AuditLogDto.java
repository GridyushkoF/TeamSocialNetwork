package ru.skillbox.auditservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.skillbox.commonlib.event.audit.ActionType;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class AuditLogDto {
    private Long id;
    private String entityName;
    private Long entityId;
    private ActionType actionType;
    private Long userId;
    private ZonedDateTime createdAt;
}
