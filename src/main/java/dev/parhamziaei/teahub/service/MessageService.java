package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.enums.messages.AuthMessage;
import dev.parhamziaei.teahub.enums.messages.Message;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.enums.messages.Text;
import dev.parhamziaei.teahub.enums.payment.InvoiceStatus;
import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.enums.shop.ProductPeriod;
import dev.parhamziaei.teahub.enums.shop.ResourceStatus;
import dev.parhamziaei.teahub.enums.teaspeak.QueryInstanceStatus;
import dev.parhamziaei.teahub.enums.teaspeak.TeaSpeakStatus;
import dev.parhamziaei.teahub.enums.ticket.TicketDepartment;
import dev.parhamziaei.teahub.enums.ticket.TicketStatus;
import dev.parhamziaei.teahub.enums.user.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageSource messageSource;
    private final MessageSource textSource;

    public String get(Message message) {
        return messageSource.getMessage(message.key(), null, Locale.forLanguageTag("fa"));
    }

    public String get(AuthMessage message) {
        return messageSource.getMessage(message.key(), null, Locale.forLanguageTag("fa"));
    }

    public String get(ServiceMessage message) {
        return messageSource.getMessage(message.key(), null, Locale.forLanguageTag("fa"));
    }

    public String get(QueryInstanceStatus status) {
        return textSource.getMessage(status.key(), null, Locale.forLanguageTag("fa"));
    }

    public String get(TicketStatus status) {
        return textSource.getMessage(status.key(), null, Locale.forLanguageTag("fa"));
    }

    public String get(TicketDepartment department) {
        return textSource.getMessage(department.key(), null, Locale.forLanguageTag("fa"));
    }

    public String get(InvoiceStatus status) {
        return textSource.getMessage(status.key(), null, Locale.forLanguageTag("fa"));
    }

    public String get(ResourceStatus status) {
        return textSource.getMessage(status.key(), null, Locale.forLanguageTag("fa"));
    }

    public String get(ProductPeriod period) {
        return textSource.getMessage(period.key(), null, Locale.forLanguageTag("fa"));
    }

    public String get(Text text) {
        return textSource.getMessage(text.key(), null, Locale.forLanguageTag("fa"));
    }

    public String get(TransactionReason reason) {
        return textSource.getMessage(reason.key(), null, Locale.forLanguageTag("fa"));
    }

    public String get(TeaSpeakStatus teaSpeakStatus) {
        return textSource.getMessage(teaSpeakStatus.key(), null, Locale.forLanguageTag("fa"));
    }

    public String get(Roles roles) {
        return textSource.getMessage(roles.key(), null, Locale.forLanguageTag("fa"));
    }
}
