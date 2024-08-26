package ru.skillbox.auditservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.auditservice.mapper.AuditLogMapper;
import ru.skillbox.auditservice.model.dto.AuditLogDto;
import ru.skillbox.auditservice.model.dto.AuditSearchFilterDto;
import ru.skillbox.auditservice.model.entity.AuditLog;
import ru.skillbox.auditservice.repository.AuditLogRepository;
import ru.skillbox.auditservice.util.AuditSpecificationUtil;
import ru.skillbox.commonlib.event.audit.AuditEvent;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditService {
    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

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
    public List<AuditLogDto> findAllByFilter(AuditSearchFilterDto auditSearchFilterDto, Pageable pageable) {
        Specification<AuditLog> auditLogSpecification = AuditSpecificationUtil.getSpecificationByDto(auditSearchFilterDto);
        Page<AuditLog> auditLogPage = auditLogRepository.findAll(auditLogSpecification, pageable);
        return auditLogPage.getContent().stream().map(auditLogMapper::toDto).toList();
    }
}
