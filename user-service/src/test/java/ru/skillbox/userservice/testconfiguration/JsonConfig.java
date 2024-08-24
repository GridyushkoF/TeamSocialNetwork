package ru.skillbox.userservice.testconfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import ru.skillbox.commonlib.json.deserializer.DateTimeDeserializer;
import ru.skillbox.commonlib.json.deserializer.PageableDeserializer;
import ru.skillbox.commonlib.json.serializer.DateTimeSerializer;
import ru.skillbox.commonlib.json.serializer.PageableSerializer;

import java.time.ZonedDateTime;

@TestConfiguration
public class JsonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()
                .addSerializer(ZonedDateTime.class, new DateTimeSerializer())
                .addDeserializer(ZonedDateTime.class, new DateTimeDeserializer()));
        objectMapper.registerModule(new SimpleModule()
                .addSerializer(Pageable.class, new PageableSerializer())
                .addDeserializer(Pageable.class, new PageableDeserializer()));
        return objectMapper;
    }
}
