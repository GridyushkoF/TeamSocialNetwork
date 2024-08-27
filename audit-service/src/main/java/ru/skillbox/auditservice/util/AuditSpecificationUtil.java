package ru.skillbox.auditservice.util;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.auditservice.model.dto.AuditSearchFilterDto;
import ru.skillbox.auditservice.model.entity.AuditLog;

import java.util.ArrayList;
import java.util.List;

public class AuditSpecificationUtil {

    public static Specification<AuditLog> getSpecificationByDto(AuditSearchFilterDto auditSearchFilterDto) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (auditSearchFilterDto.getEntityName() != null) {
                predicates.add(builder.equal(root.get("entityName"), auditSearchFilterDto.getEntityName()));
            }
            if (auditSearchFilterDto.getEntityId() != null) {
                predicates.add(builder.equal(root.get("entityId"), auditSearchFilterDto.getEntityId()));
            }
            if (auditSearchFilterDto.getUserId() != null) {
                predicates.add(builder.equal(root.get("userId"), auditSearchFilterDto.getUserId()));
            }
            if (auditSearchFilterDto.getActionType() != null) {
                predicates.add(builder.equal(root.get("actionType"), auditSearchFilterDto.getActionType()));
            }
            if (auditSearchFilterDto.getFrom() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), auditSearchFilterDto.getFrom()));
            }
            if (auditSearchFilterDto.getTo() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), auditSearchFilterDto.getTo()));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
    private AuditSpecificationUtil() {

    }
}