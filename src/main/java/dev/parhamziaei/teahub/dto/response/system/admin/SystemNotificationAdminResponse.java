package dev.parhamziaei.teahub.dto.response.system.admin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.parhamziaei.teahub.dto.response.system.user.SystemNotificationUserResponse;
import dev.parhamziaei.teahub.dto.serializer.ExpirationDurationSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemNotificationAdminResponse extends SystemNotificationUserResponse {

    private Long publisherId;

    private LocalDateTime expiresAt;

}
