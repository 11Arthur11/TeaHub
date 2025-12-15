package dev.parhamziaei.teahub.dto.internal;

import dev.parhamziaei.teahub.enums.user.NotificationMethod;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Getter
public class SMSNotification extends BaseNotification {
    private final String smsIPPanelPattern;
    private final Map<String, String> smsPlaceholders;

    @Builder
    SMSNotification(NotificationMethod notificationMethod, String recipient, String smsIPPanelPattern, Map<String, String> smsPlaceholders) {
        super(notificationMethod, recipient);
        this.smsIPPanelPattern = smsIPPanelPattern;
        this.smsPlaceholders = smsPlaceholders;
    }
}
