package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.query.BasePaginationRequest;
import dev.parhamziaei.teahub.dto.request.resource.user.AudioBotResourceEditRequest;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.dto.response.resource.audio_bot.user.AudioBotScopedPanelAccessResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.integration.audio_bot.dto.playlist.ABPlayListDetailResponse;
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
            @RequestBody AudioBotResourceEditRequest editRequest
    ) {
        audioBotService.editInstance(currentUser.getId(), resourceId, editRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

    @GetMapping("/{resourceId}/access")
    public ResponseEntity<DataResponse<AudioBotScopedPanelAccessResponse>> getPanelAccess(@PathVariable Long resourceId) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                audioBotService.getInstanceScopedPanelAccess(currentUser.getId(), resourceId),
                HttpStatus.OK
        );
    }

//    @GetMapping("/{resourceId}/playlists")
//    public ResponseEntity<DataResponse<List<ABPlayListsResponse>>> getAudioBotPlaylists(@PathVariable Long resourceId) {
//        return ResponseBuilder.buildSuccess(
//                ResponseType.SUCCESS,
//                audioBotService.getInstancePlayLists(currentUser.getId(), resourceId),
//                HttpStatus.OK
//        );
//    }

//    @PostMapping("/{resourceId}/playlists")
//    public ResponseEntity<SimpleResponse> addAudioBotPlaylist(
//            @PathVariable Long resourceId,
//            @RequestBody AudioBotPlaylistCreateRequest playlistRequest
//    ) {
//        audioBotService.createPlayList(
//                currentUser.getId(),
//                resourceId,
//                playlistRequest
//        );
//        return ResponseBuilder.buildSuccess(
//                ResponseType.SUCCESS,
//                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
//                HttpStatus.OK
//        );
//    }

//    @DeleteMapping("/{resourceId}/playlists/{playlistFilename}")
//    public ResponseEntity<SimpleResponse> deleteAudioBotPlaylist(
//            @PathVariable Long resourceId,
//            @PathVariable String playlistFilename
//    ) {
//        audioBotService.deletePlayList(
//                currentUser.getId(),
//                resourceId,
//                playlistFilename
//        );
//        return ResponseBuilder.buildSuccess(
//                ResponseType.SUCCESS,
//                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
//                HttpStatus.OK
//        );
//    }

    @PostMapping("/{resourceId}/playlists/{playlistFilename}/details")
    public ResponseEntity<DataResponse<ABPlayListDetailResponse>> getAudioBotPlaylistDetail(
            @PathVariable Long resourceId,
            @PathVariable String playlistFilename,
            @RequestBody BasePaginationRequest paginationRequest
    ) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                audioBotService.getInstancePlayListDetail(
                        currentUser.getId(),
                        resourceId,
                        playlistFilename,
                        paginationRequest
                ),
                HttpStatus.OK
        );
    }

//    @PostMapping("/{resourceId}/playlists/{playlistFilename}/tracks")
//    public ResponseEntity<SimpleResponse> addTrackToAudioBotPlaylist(
//            @PathVariable Long resourceId,
//            @PathVariable String playlistFilename,
//            @RequestBody AudioBotPlaylistTrackAddRequest linkAddRequest
//    ) {
//        audioBotService.addLinkToPlayList(
//                currentUser.getId(),
//                resourceId,
//                playlistFilename,
//                linkAddRequest
//        );
//        return ResponseBuilder.buildSuccess(
//                ResponseType.SUCCESS,
//                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
//                HttpStatus.OK
//        );
//    }

}
