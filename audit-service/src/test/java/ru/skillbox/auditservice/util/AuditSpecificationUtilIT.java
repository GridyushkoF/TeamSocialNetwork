package ru.skillbox.auditservice.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.test.context.EmbeddedKafka;
import ru.skillbox.auditservice.model.dto.AuditSearchFilterDto;
import ru.skillbox.auditservice.model.entity.AuditLog;
import ru.skillbox.auditservice.repository.AuditLogRepository;
import ru.skillbox.commonlib.event.audit.ActionType;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class AuditSpecificationUtilIT {
    @Autowired
    private AuditLogRepository auditLogRepository;
    @Test
    void createSpecification1() {
        auditLogRepository.save(AuditLog.builder().entityName("POST").entityId(1L).userId(1L).actionType(ActionType.DELETE).build());
        auditLogRepository.save(AuditLog.builder().entityName("USER").entityId(1L).userId(1L).actionType(ActionType.CREATE).build());
        AuditSearchFilterDto auditSearchFilterDto = AuditSearchFilterDto.builder()
                .entityName("POST")
                .actionType(ActionType.DELETE)
                .build();
        Specification<AuditLog> specification = AuditSpecificationUtil.getSpecificationByDto(auditSearchFilterDto);
        assertEquals(1,auditLogRepository.findAll(specification).size());

    }
}
