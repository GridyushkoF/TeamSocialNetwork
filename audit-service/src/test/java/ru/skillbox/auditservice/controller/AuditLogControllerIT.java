package ru.skillbox.auditservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.auditservice.model.dto.AuditSearchFilterDto;
import ru.skillbox.auditservice.model.entity.AuditLog;
import ru.skillbox.auditservice.repository.AuditLogRepository;
import ru.skillbox.commonlib.event.audit.ActionType;

import java.time.ZonedDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuditLogControllerIT {
    @Autowired
    private MockMvc mockMvc ;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuditLogRepository auditLogRepository;

    @Test
    @DisplayName("Should return audit logs based on filter")
    void testSearchLogs() throws Exception {
        AuditLog auditLog = AuditLog.builder()
                .id(1L)
                .entityName("POST")
                .entityId(1L)
                .actionType(ActionType.CREATE)
                .userId(1L)
                .createdAt(ZonedDateTime.now())
                .build();
        auditLogRepository.save(auditLog);


        AuditSearchFilterDto filterDto = new AuditSearchFilterDto();
        filterDto.setEntityName("POST");
        mockMvc.perform(post("/api/v1/audit/search?page=0&size=100&sort=id,asc")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filterDto))
                        .header("id","1")
                        .header("authorities","ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].entityName").value("POST"))
                .andExpect(jsonPath("$[0].userId").value(1L));
    }
}