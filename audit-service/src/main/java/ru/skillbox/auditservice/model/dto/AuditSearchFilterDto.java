package ru.skillbox.auditservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.commonlib.event.audit.ActionType;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditSearchFilterDto {
    private String entityName;
    private String entityId;
    private Long userId;
    private ActionType actionType;
    private ZonedDateTime from;
    private ZonedDateTime to;
}
