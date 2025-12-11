package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.TeaSpeakService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/services/teaspeak")
@RequiredArgsConstructor
public class TeaSpeakServiceController {

    private final TeaSpeakService teaSpeakService;
    private final CurrentUser currentUser;
    private final MessageService messageService;

    @PostMapping("/{resourceId}/start")
    public ResponseEntity<SimpleResponse> startTeaSpeak(@PathVariable Long resourceId) {
        teaSpeakService.startTeaSpeakInstance(currentUser.getId(), resourceId);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

    @PostMapping("/{resourceId}/stop")
    public ResponseEntity<SimpleResponse> stopTeaSpeak(@PathVariable Long resourceId) {
        teaSpeakService.stopTeaSpeakInstance(currentUser.getId(), resourceId);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

}
