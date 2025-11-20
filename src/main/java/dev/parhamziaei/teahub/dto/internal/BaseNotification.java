package dev.parhamziaei.teahub.dto.internal;

import dev.parhamziaei.teahub.enums.NotificationMethod;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
public abstract class BaseNotification {
    protected NotificationMethod notificationMethod;
    protected String recipient;

    public BaseNotification(NotificationMethod notificationMethod, String recipient) {
    }
}
