package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.enums.messages.AuthMessage;
import dev.parhamziaei.teahub.enums.messages.Message;
import dev.parhamziaei.teahub.enums.Text;
import dev.parhamziaei.teahub.enums.TicketDepartment;
import dev.parhamziaei.teahub.enums.TicketStatus;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
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

    public String get(TicketStatus status) {
        return textSource.getMessage(status.key(), null, Locale.forLanguageTag("fa"));
    }

    public String get(TicketDepartment department) {
        return textSource.getMessage(department.key(), null, Locale.forLanguageTag("fa"));
    }

    public String get(Text text) {
        return textSource.getMessage(text.key(), null, Locale.forLanguageTag("fa"));
    }

}
