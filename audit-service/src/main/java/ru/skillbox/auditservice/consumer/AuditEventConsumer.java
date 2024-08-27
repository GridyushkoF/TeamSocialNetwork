package ru.skillbox.auditservice.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skillbox.auditservice.service.AuditService;
import ru.skillbox.commonlib.event.audit.AuditEvent;

@Component
public class AuditEventConsumer implements EventConsumer<AuditEvent> {

    private final AuditService auditService;

    @Autowired
    public AuditEventConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public void consumeEvent(AuditEvent event) {
        createAuditLog(event);
    }

    private void createAuditLog(AuditEvent event) {
        auditService.createAuditLog(event);
    }
}
