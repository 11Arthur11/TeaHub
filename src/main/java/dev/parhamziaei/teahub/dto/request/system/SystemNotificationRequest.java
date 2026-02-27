package dev.parhamziaei.teahub.dto.request.system;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.parhamziaei.teahub.dto.deserializer.ExpirationDurationDeserializer;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemNotificationRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String text;

    @JsonDeserialize(using = ExpirationDurationDeserializer.class)
    private String expiresAt;

}
