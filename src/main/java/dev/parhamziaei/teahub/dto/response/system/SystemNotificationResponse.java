package dev.parhamziaei.teahub.dto.response.system;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.parhamziaei.teahub.dto.serializer.ExpirationDurationSerializer;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemNotificationResponse {

    private String title;

    private String text;

    @JsonSerialize(using = ExpirationDurationSerializer.class)
    private Duration expiresAt;

}
