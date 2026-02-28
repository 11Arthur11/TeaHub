package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.dto.response.system.user.SystemNotificationUserResponse;
import dev.parhamziaei.teahub.entity.jpa.system.SystemNotification;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.service.NotificationService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> getAllGlobalNotifications() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                notificationService.getGlobalNotifications(SystemNotificationUserResponse.class),
                HttpStatus.OK
        );
    }

}
