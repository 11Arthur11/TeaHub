package dev.parhamziaei.teahub.service.implement;

import dev.parhamziaei.teahub.configuration.properties.NotificationProperties;
import dev.parhamziaei.teahub.dto.internal.SMSNotification;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.service.interfaces.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

}
