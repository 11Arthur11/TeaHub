package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.AudioBotService;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/services/audio-bot")
public class AudioBotServiceController {

    private final AudioBotService audioBotService;
    private final CurrentUser currentUser;
    private final MessageService messageService;

    @PostMapping("/{resourceId}/start")
    public ResponseEntity<SimpleResponse> startTeaSpeak(@PathVariable Long resourceId) {
        audioBotService.startInstance(currentUser.getId(), resourceId);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

    @PostMapping("/{resourceId}/stop")
    public ResponseEntity<SimpleResponse> stopTeaSpeak(@PathVariable Long resourceId) {
        audioBotService.stopInstance(currentUser.getId(), resourceId);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

}
