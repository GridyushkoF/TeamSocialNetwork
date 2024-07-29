package ru.skillbox.commondto.util.admin;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import ru.skillbox.commondto.dto.statistics.DateCountPointDto;


import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class AdminStatisticsRepository {
    private final EntityManager entityManager;

    public List<DateCountPointDto> getDateCountStatistics(String dateFieldName,
                                                          String groupByName,
                                                          String entityName,
                                                          LocalDateTime startDate,
                                                          LocalDateTime endDate) {
        String queryString = AdminQueryGeneratorUtil.generateStatisticsQuery(entityName,dateFieldName,groupByName);
        TypedQuery<DateCountPointDto> query = entityManager.createQuery(queryString, DateCountPointDto.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        return query.getResultList();
    }

}