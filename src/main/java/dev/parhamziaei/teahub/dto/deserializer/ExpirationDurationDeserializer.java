package dev.parhamziaei.teahub.dto.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpirationDurationDeserializer extends JsonDeserializer<Duration> {
    @Override
    public Duration deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String text = p.getText().trim().toLowerCase();
        Pattern pattern = Pattern.compile("^[0-9]+[dh]$");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.matches())
            throw new IllegalArgumentException(String.format("Invalid duration format: %s", text));

        long numberPart = Long.parseLong(text.substring(0, text.length() - 1));

        if (text.endsWith("d"))
            return Duration.ofDays(numberPart);
        if (text.endsWith("h"))
            return Duration.ofHours(numberPart);

        throw new IllegalArgumentException(String.format("Invalid expiration duration format: %s only support days & hours!", text));
    }
}
