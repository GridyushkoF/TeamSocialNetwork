package ru.skillbox.auditservice.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.test.context.EmbeddedKafka;
import ru.skillbox.auditservice.model.dto.AuditLogDto;
import ru.skillbox.auditservice.model.dto.AuditSearchFilterDto;
import ru.skillbox.auditservice.model.entity.AuditLog;
import ru.skillbox.auditservice.repository.AuditLogRepository;
import ru.skillbox.commonlib.event.audit.ActionType;
import ru.skillbox.commonlib.event.audit.AuditEvent;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class AuditServiceIT {
    @Autowired
    private AuditLogRepository auditLogRepository;
    @Autowired
    private AuditService auditService;

    @BeforeEach
    void setUp() {
        auditLogRepository.deleteAll();
    }
    @Test
    @DisplayName("testing that audit action correctly saving into DB")
    void testCreateAuditLog() {
        AuditEvent auditEvent = AuditEvent.builder()
                .entityName("POST")
                .actionType(ActionType.CREATE)
                .userId(1L)
                .id(1L)
                .createdAt(ZonedDateTime.now())
                .build();
        auditService.createAuditLog(auditEvent);
        AuditLog auditLog = auditLogRepository.findAll().get(0);
        assertEquals("POST", auditLog.getEntityName());
    }
    @Test
    @DisplayName("creating test data and testing a filter")
    void testFindAllByFilter() {
        AuditEvent auditEvent1 = AuditEvent.builder()
                .entityName("POST")
                .actionType(ActionType.CREATE)
                .userId(1L)
                .id(1L)
                .createdAt(ZonedDateTime.now())
                .build();
        auditService.createAuditLog(auditEvent1);

        AuditEvent auditEvent2 = AuditEvent.builder()
                .entityName("USER")
                .actionType(ActionType.UPDATE)
                .userId(2L)
                .id(2L)
                .createdAt(ZonedDateTime.now())
                .build();
        auditService.createAuditLog(auditEvent2);

        AuditSearchFilterDto filterDto = new AuditSearchFilterDto();
        filterDto.setEntityName("POST");

        List<AuditLogDto> results = auditService.findAllByFilter(filterDto, Pageable.unpaged());
        assertEquals(1, results.size());
        assertEquals("POST", results.get(0).getEntityName());
        assertEquals(ActionType.CREATE, results.get(0).getActionType());
        assertEquals(1L, results.get(0).getUserId());
    }
}
