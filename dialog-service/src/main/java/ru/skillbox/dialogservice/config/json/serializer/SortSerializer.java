package ru.skillbox.dialogservice.config.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.data.domain.Sort;

import java.io.IOException;

public class SortSerializer extends JsonSerializer<Sort> {
    @Override
    public void serialize(Sort sort, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeBooleanField("empty", sort.isEmpty());
        jsonGenerator.writeBooleanField("sorted", sort.isSorted());
        jsonGenerator.writeBooleanField("unsorted", sort.isUnsorted());
        jsonGenerator.writeEndObject();
    }
}