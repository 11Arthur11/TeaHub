package dev.parhamziaei.teahub.dto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Duration;

public class ExpirationDurationSerializer extends JsonSerializer<Duration> {
    @Override
    public void serialize(Duration duration, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (duration.compareTo(Duration.ofDays(1)) <= 0)
            jsonGenerator.writeString((duration.toHours()) + "h");
        else
            jsonGenerator.writeString((duration.toDays()) + "d");
    }
}
