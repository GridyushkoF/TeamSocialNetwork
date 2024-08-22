package ru.skillbox.commonlib.util.admin;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import ru.skillbox.commonlib.dto.statistics.DateCountPointDto;

import java.time.ZonedDateTime;
import java.util.List;

@RequiredArgsConstructor
public class AdminStatisticsRepository {
    private final EntityManager entityManager;

    public List<DateCountPointDto> getDateCountStatistics(String dateFieldName,
                                                          String groupByName,
                                                          String entityName,
                                                          ZonedDateTime startDate,
                                                          ZonedDateTime endDate) {
        String queryString = AdminQueryGeneratorUtil.generateStatisticsQuery(
                entityName,
                dateFieldName,
                groupByName);
        TypedQuery<DateCountPointDto> query = entityManager.createQuery(queryString, DateCountPointDto.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        return query.getResultList();
    }
    public Long countEntities(String entityName, String dateFieldName, ZonedDateTime dateTimeFrom, ZonedDateTime dateTimeTo) {
        TypedQuery<Long> query = entityManager.createQuery(AdminQueryGeneratorUtil
                .generateCountQuery(entityName,dateFieldName), Long.class);
        query.setParameter("dateTimeFrom", dateTimeFrom);
        query.setParameter("dateTimeTo", dateTimeTo);
        return query.getSingleResult().longValue();
    }

}