package dev.parhamziaei.teahub.dto.response.system.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.parhamziaei.teahub.dto.serializer.ExpirationDurationSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemNotificationUserResponse {

    private Long id;

    private String title;

    private String text;

    private LocalDateTime createdAt;

}
