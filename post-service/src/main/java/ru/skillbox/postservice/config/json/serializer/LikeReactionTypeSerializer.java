package ru.skillbox.postservice.config.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ru.skillbox.commonlib.dto.post.LikeReactionType;

import java.io.IOException;

public class LikeReactionTypeSerializer extends JsonSerializer<LikeReactionType> {
    @Override
    public void serialize(LikeReactionType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.name().toLowerCase());
    }
}