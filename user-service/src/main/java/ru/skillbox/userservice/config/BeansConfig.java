package ru.skillbox.userservice.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.commonlib.util.admin.AdminStatisticsRepository;

@Configuration
public class BeansConfig {
    @Bean
    public AdminStatisticsRepository adminStatisticsRepository(EntityManager entityManager) {
        return new AdminStatisticsRepository(entityManager);
    }
}
