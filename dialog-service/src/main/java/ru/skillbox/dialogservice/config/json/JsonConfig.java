package ru.skillbox.dialogservice.config.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.skillbox.dialogservice.config.json.deserializer.LocalDateTimeDeserializer;
import ru.skillbox.dialogservice.config.json.deserializer.PageableDeserializer;
import ru.skillbox.dialogservice.config.json.deserializer.SortDeserializer;
import ru.skillbox.dialogservice.config.json.serializer.LocalDateTimeSerializer;
import ru.skillbox.dialogservice.config.json.serializer.PageableSerializer;
import ru.skillbox.dialogservice.config.json.serializer.SortSerializer;

import java.time.LocalDateTime;

@Configuration
public class JsonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer())
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer()));
        objectMapper.registerModule(new SimpleModule()
                .addSerializer(Sort.class, new SortSerializer())
                .addDeserializer(Sort.class, new SortDeserializer())
                .addSerializer(Pageable.class, new PageableSerializer())
                .addDeserializer(Pageable.class, new PageableDeserializer()));
        return objectMapper;
    }
}
