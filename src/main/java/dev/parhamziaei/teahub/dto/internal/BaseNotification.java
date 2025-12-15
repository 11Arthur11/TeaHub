package dev.parhamziaei.teahub.dto.internal;

import dev.parhamziaei.teahub.enums.user.NotificationMethod;
import lombok.Data;

@Data
public abstract class BaseNotification {
    protected NotificationMethod notificationMethod;
    protected String recipient;

    public BaseNotification(NotificationMethod notificationMethod, String recipient) {
    }
}
