package ru.skillbox.auditservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.auditservice.model.entity.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog,Long> {
}
