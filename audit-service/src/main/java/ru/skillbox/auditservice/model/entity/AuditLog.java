package ru.skillbox.auditservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import ru.skillbox.commonlib.event.audit.ActionType;

import java.time.ZonedDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "audit_logs")
@Data
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String entityName;
    private Long entityId;
    @Enumerated(EnumType.STRING)
    private ActionType actionType;
    private Long userId;
    @CreationTimestamp
    private ZonedDateTime createdAt;

}
