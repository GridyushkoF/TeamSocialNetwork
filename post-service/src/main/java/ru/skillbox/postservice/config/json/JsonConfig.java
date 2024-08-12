package ru.skillbox.postservice.config.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import ru.skillbox.commonlib.dto.post.LikeReactionType;
import ru.skillbox.postservice.config.json.deserializer.LikeReactionTypeDeserializer;
import ru.skillbox.postservice.config.json.deserializer.DateTimeDeserializer;
import ru.skillbox.postservice.config.json.deserializer.PageableDeserializer;
import ru.skillbox.postservice.config.json.serializer.LikeReactionTypeSerializer;
import ru.skillbox.postservice.config.json.serializer.DateTimeSerializer;
import ru.skillbox.postservice.config.json.serializer.PageableSerializer;

import java.time.ZonedDateTime;

@Configuration
public class JsonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()
                .addSerializer(ZonedDateTime.class, new DateTimeSerializer())
                .addDeserializer(ZonedDateTime.class, new DateTimeDeserializer()));
        objectMapper.registerModule(new SimpleModule()
                .addSerializer(Pageable.class, new PageableSerializer())
                .addDeserializer(Pageable.class, new PageableDeserializer())
                .addDeserializer(LikeReactionType.class,new LikeReactionTypeDeserializer())
                .addSerializer(LikeReactionType.class,new LikeReactionTypeSerializer()));
        return objectMapper;
    }
}
