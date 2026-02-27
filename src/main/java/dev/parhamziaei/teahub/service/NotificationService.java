package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.system.SystemNotificationRequest;
import dev.parhamziaei.teahub.entity.jpa.system.SystemNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @PreAuthorize("hasRole('ADMIN')")
    public void sendGlobalNotification(SystemNotificationRequest request) {
        SystemNotification notification = SystemNotification.builder()
                .title(request.getTitle())
                .text(request.getText())
                .createdAt(LocalDateTime.now().withNano(0))
                .build();
    }

}
