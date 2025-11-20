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

    private final NotificationProperties notificationProperties;

    @Async
    @Override
    public <U extends User> void sendLoginNotificationLogin(U user) {
//        boolean smsEnabled = user.getSetting().isSmsEnabled();
//        boolean emailEnabled = user.getSetting().isEmailEnabled();
//
//        if (smsEnabled) {
//            Map<String, String> smsPlaceholders = new HashMap<>();
//            smsPlaceholders.put("time" , )
//            SMSNotification smsNotification = SMSNotification.builder()
//                    .notificationMethod(NotificationMethod.SMS)
//                    .recipient(user.getPhone())
//                    .smsIPPanelPattern(notificationProperties.smsLoginNotifIPPanelPattern())
//                    .smsPlaceholders()
//                    .build();
//        }
    }

    private <U extends User> void handleSMS(SMSNotification smsNotification, U user) {

    }

}
