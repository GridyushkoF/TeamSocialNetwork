package ru.skillbox.controller;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.text.MessageFormat;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class GeoControllerAndServiceIT {
    private static final int RUSSIA_ID = 113;
    private static final String ALL_COUNTRIES_URL = "/api/v1/geo/country";
    private static final String CITIES_URL = "/api/v1/geo/country/{0}/city";

    @Container
    private static final RedisContainer redisContainer =
            new RedisContainer(DockerImageName.parse("redis:6.2.6"))
                    .withExposedPorts(6379)
                    .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger(GeoControllerAndServiceIT.class)));

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);
    }

    @Test
    @DisplayName("2 requests sending, checking correctness of responses and speed of second requests. Speed must be really faster")
    void getCountriesTest() {

        long startTime = System.currentTimeMillis();
        performAllCountriesAndCheckCorrectness(ALL_COUNTRIES_URL);
        long firstResponseTime = System.currentTimeMillis() - startTime;
        System.out.println("Первый запрос занял: " + firstResponseTime + " мс");

        startTime = System.currentTimeMillis();
        performAllCountriesAndCheckCorrectness(ALL_COUNTRIES_URL);
        long secondResponseTime = System.currentTimeMillis() - startTime;
        System.out.println("Второй запрос занял: " + secondResponseTime + " мс");

        assertTrue(secondResponseTime < firstResponseTime, "Второй запрос должен быть быстрее первого");
    }


    @Test
    @DisplayName("2 requests sending, checking correctness of responses and speed of second requests. Speed must be really faster")
    void getCitiesByCountryIdTest() {
        String russiaCitiesUrl = MessageFormat.format(CITIES_URL, RUSSIA_ID);
        long firstResponseTime = measureSpeed(() -> {
            performAllCountriesAndCheckCorrectness(russiaCitiesUrl);
        });
        System.out.println("Первый запрос занял: " + firstResponseTime + " мс");

        long secondResponseTime = measureSpeed(() -> {
            performAllCountriesAndCheckCorrectness(russiaCitiesUrl);
        });
        System.out.println("Второй запрос занял: " + secondResponseTime + " мс");

        assertTrue(secondResponseTime < firstResponseTime, "Второй запрос должен быть быстрее первого");
    }

    private void performAllCountriesAndCheckCorrectness(String url) {
        try {
            mockMvc.perform(get(url))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", isA(List.class)))
                    .andExpect(jsonPath("$", hasSize(greaterThan(0))));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public long measureSpeed(Runnable runnable) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        return System.currentTimeMillis() - startTime;
    }


}