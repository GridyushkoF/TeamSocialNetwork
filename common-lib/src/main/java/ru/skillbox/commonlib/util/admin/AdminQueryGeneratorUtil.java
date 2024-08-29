package ru.skillbox.commonlib.util.admin;

import ru.skillbox.commonlib.dto.statistics.DateCountPointDto;

public class AdminQueryGeneratorUtil {
    public static String generateStatisticsQuery(String entityClassName, String dateFieldName, String groupByName) {
        String dtoClassName = DateCountPointDto.class.getName();
        return String.format("SELECT new %s(" +
                        "DATE_TRUNC('%s', e.%s), COUNT(e)) " +
                        "FROM %s e " +
                        "WHERE e.%s BETWEEN :startDate AND :endDate " +
                        "GROUP BY DATE_TRUNC('%s', e.%s)",
                dtoClassName,
                groupByName,
                dateFieldName,
                entityClassName,
                dateFieldName,
                groupByName,
                dateFieldName
        );
    }

    public static String generateCountQuery(String entityName, String dateFieldName) {
        return String.format("SELECT COUNT(e) FROM %s e WHERE e.%s BETWEEN :dateTimeFrom AND :dateTimeTo",
                entityName, dateFieldName);
    }
    private AdminQueryGeneratorUtil() {

    }
}
