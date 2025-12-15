package dev.parhamziaei.teahub.dto.internal;

import dev.parhamziaei.teahub.enums.user.NotificationMethod;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.thymeleaf.context.Context;

@EqualsAndHashCode(callSuper = true)
@Getter
public class EmailNotification extends BaseNotification {

    private final Context context;
    private final String subject;
    private final String template;

    @Builder
    public EmailNotification(NotificationMethod notificationMethod, String recipient, Context context, String subject, String template) {
        super(notificationMethod, recipient);
        this.context = context;
        this.subject = subject;
        this.template = template;
    }

}
