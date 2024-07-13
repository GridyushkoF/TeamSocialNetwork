package ru.skillbox.dialogservice.config.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public class PageableSerializer extends JsonSerializer<Pageable> {
    @Override
    public void serialize(Pageable pageable, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("page", pageable.getPageNumber());
        jsonGenerator.writeNumberField("size", pageable.getPageSize());
        jsonGenerator.writeObjectField("sort", pageable.getSort());
        jsonGenerator.writeEndObject();
    }
}