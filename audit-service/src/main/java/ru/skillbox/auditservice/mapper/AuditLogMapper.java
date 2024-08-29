package ru.skillbox.auditservice.mapper;


import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.skillbox.auditservice.model.dto.AuditLogDto;
import ru.skillbox.auditservice.model.entity.AuditLog;

@Mapper(componentModel = "spring")
@Component
public interface AuditLogMapper {
    AuditLogDto toDto(AuditLog auditLog);

    AuditLog toEntity(AuditLogDto auditLogDto);
}