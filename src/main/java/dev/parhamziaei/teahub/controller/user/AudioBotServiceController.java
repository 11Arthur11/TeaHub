package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.resource.user.EditAudioBotResourceRequest;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.entity.jpa.resource.AudioBotResource;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.AudioBotService;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/services/audio-bot")
public class AudioBotServiceController {

    private final AudioBotService audioBotService;
    private final CurrentUser currentUser;
    private final MessageService messageService;

    @PostMapping("/{resourceId}/start")
    public ResponseEntity<SimpleResponse> startAudioBot(@PathVariable Long resourceId) {
        audioBotService.startInstance(currentUser.getId(), resourceId);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

    @PostMapping("/{resourceId}/stop")
    public ResponseEntity<SimpleResponse> stopAudioBot(@PathVariable Long resourceId) {
        audioBotService.stopInstance(currentUser.getId(), resourceId);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

    @PostMapping("/{resourceId}/edit")
    public ResponseEntity<SimpleResponse> editAudioBot(
            @PathVariable Long resourceId,
            @RequestBody EditAudioBotResourceRequest editRequest
    ) {
        audioBotService.editInstance(currentUser.getId(), resourceId, editRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

    @GetMapping("/{resourceId}/playlist")
    public ResponseEntity<?> getAudioBotPlaylists(@PathVariable Long resourceId) {
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                audioBotService.getInstancePlayLists(currentUser.getId(), resourceId),
                HttpStatus.OK
        );
    }

}
