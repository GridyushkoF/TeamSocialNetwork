package ru.skillbox.auditservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.auditservice.model.entity.AuditLog;
import ru.skillbox.auditservice.repository.AuditLogRepository;
import ru.skillbox.commonlib.event.audit.AuditEvent;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditLogRepository auditLogRepository;

    @Transactional
    public void createAuditLog(AuditEvent event) {
        AuditLog auditLog = AuditLog.builder()
                .entityName(event.getEntityName())
                .entityId(event.getId())
                .actionType(event.getActionType())
                .userId(event.getUserId())
                .build();
        auditLogRepository.save(auditLog);
    }
}
