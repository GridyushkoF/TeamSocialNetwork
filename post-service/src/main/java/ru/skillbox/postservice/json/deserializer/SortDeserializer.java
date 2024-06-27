package ru.skillbox.postservice.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Sort;
import ru.skillbox.postservice.util.SortCreator;

import java.io.IOException;

public class SortDeserializer extends JsonDeserializer<Sort> {
    @Override
    public Sort deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec objectCodec = jsonParser.getCodec();
        JsonNode jsonNode = objectCodec.readTree(jsonParser);
        boolean empty = jsonNode.get("empty").asBoolean();
        boolean sorted = jsonNode.get("sorted").asBoolean();
        boolean unsorted = jsonNode.get("unsorted").asBoolean();
        return SortCreator.createSort(empty,sorted,unsorted);
    }
}