package ru.skillbox.postservice.config.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ru.skillbox.commonlib.dto.post.LikeReactionType;

import java.io.IOException;

public class LikeReactionTypeDeserializer extends JsonDeserializer<LikeReactionType> {
    @Override
    public LikeReactionType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getText();
        return LikeReactionType.valueOf(value.toUpperCase());
    }
}