package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.system.SystemNotificationRequest;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.dto.response.system.admin.SystemNotificationAdminResponse;
import dev.parhamziaei.teahub.dto.response.system.user.SystemNotificationUserResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.NotificationService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/notifications")
@RequiredArgsConstructor
public class NotificationAdminController {

    private final NotificationService notificationService;
    private final CurrentUser currentUser;
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<DataResponse<List<SystemNotificationAdminResponse>>> getAllGlobalNotifications() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                notificationService.getGlobalNotifications(SystemNotificationAdminResponse.class),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<SimpleResponse> sendGlobalNotification(@RequestBody @Valid SystemNotificationRequest request) {
        notificationService.sendGlobalNotification(currentUser.getId(), request);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_CREATED),
                HttpStatus.OK
        );
    }

    @PostMapping("/{notificationId}")
    public ResponseEntity<SimpleResponse> editGlobalNotification(@RequestBody SystemNotificationRequest request, @PathVariable Long notificationId) {
        notificationService.editGlobalNotification(request, notificationId);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_EDITED),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<SimpleResponse> deleteGlobalNotification(@PathVariable Long notificationId) {
        notificationService.deleteGlobalNotification(notificationId);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_DELETED),
                HttpStatus.OK
        );
    }
}
