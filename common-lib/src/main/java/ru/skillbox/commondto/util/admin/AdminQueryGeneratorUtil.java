package ru.skillbox.commondto.util.admin;

public class AdminQueryGeneratorUtil {
    public static String generateStatisticsQuery(String entityClassName, String dateFieldName, String groupByName) {
        return String.format("SELECT new ru.skillbox.commondto.dto.statistics.DateCountPointDto(" +
                        "DATE_TRUNC('%s', e.%s), COUNT(e)) " +
                        "FROM %s e " +
                        "WHERE e.%s BETWEEN :startDate AND :endDate " +
                        "GROUP BY DATE_TRUNC('%s', e.%s)",
                groupByName,
                dateFieldName,
                entityClassName,
                dateFieldName,
                groupByName,
                dateFieldName
        );
    }
}
