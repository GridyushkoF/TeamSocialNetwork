package ru.skillbox.postservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.skillbox.postservice.json.deserializer.PageableDeserializer;
import ru.skillbox.postservice.json.deserializer.SortDeserializer;
import ru.skillbox.postservice.json.serializer.PageableSerializer;
import ru.skillbox.postservice.json.serializer.SortSerializer;

@Configuration
public class JsonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new SimpleModule()
                .addSerializer(Sort.class, new SortSerializer())
                .addDeserializer(Sort.class, new SortDeserializer())
                .addSerializer(Pageable.class, new PageableSerializer())
                .addDeserializer(Pageable.class, new PageableDeserializer()));
        return objectMapper;
    }
}
