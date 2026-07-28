package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.dto.request.audio_bot.admin.AudioBotNodeEditRequest;
import dev.parhamziaei.teahub.dto.request.audio_bot.admin.AudioBotNodeInitRequest;
import dev.parhamziaei.teahub.dto.request.resource.admin.ChangeProvisioningStrategyRequest;
import dev.parhamziaei.teahub.dto.response.audio_bot.admin.AudioBotNodeDetailResponse;
import dev.parhamziaei.teahub.dto.response.audio_bot.admin.AudioBotNodeListResponse;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import dev.parhamziaei.teahub.service.AudioBotNodeService;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/audio-bot-nodes")
public class AudioBotNodeController {

    private final AudioBotNodeService audioBotNodeService;
    private final MessageService messageService;

    @Operation(
            summary = "Initiate a audio bot node",
            description = "Creates and initiates a audio bot node based on the provided configuration.",
            tags = {"Audio Bot Node (Admin)"}
    )
    @PostMapping("/initiate")
    public ResponseEntity<SimpleResponse> initAudioBotNode(@Valid @RequestBody AudioBotNodeInitRequest nodeInitRequest) {
        audioBotNodeService.initiateNode(nodeInitRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.AUDIO_BOT_NODE_INITIATED),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Change Provisioning strategy",
            description = "Changing resource distribution on audio bot nodes",
            tags = {"Audio Bot Node (Admin)"}
    )
    @PatchMapping("/provisioning")
    public ResponseEntity<SimpleResponse> changeProvisioningStrategy(@RequestBody ChangeProvisioningStrategyRequest request) {
        audioBotNodeService.changeProvisioningStrategy(request.getProvisionStrategy());
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.PROVISIONING_STRATEGY_CHANGED),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Current Provisioning strategy",
            description = "returns current resource distribution on audio bot nodes",
            tags = {"Audio Bot Node (Admin)"}
    )
    @GetMapping("/provisioning")
    public ResponseEntity<DataResponse<ProvisionStrategy>> getProvisioningStrategy() {
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                audioBotNodeService.getProvisionStrategy(),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "edit a audio bot node",
            description = "edit an existing audio bot node based on the provided configuration.",
            tags = {"Audio Bot Node (Admin)"}
    )
    @PostMapping("/{nodeId}/edit")
    public ResponseEntity<SimpleResponse> editAudioBotNode(@PathVariable Long nodeId, @RequestBody AudioBotNodeEditRequest editRequest) {
        audioBotNodeService.editNode(nodeId, editRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.AUDIO_BOT_NODE_INITIATED),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "edit a audio bot node",
            description = "delete an existing audio bot node. can't delete a node with active resource",
            tags = {"Audio Bot Node (Admin)"}
    )
    @DeleteMapping("/{nodeId}")
    public ResponseEntity<SimpleResponse> deleteAudioBotNode(@PathVariable Long nodeId) {
        audioBotNodeService.deleteNode(nodeId);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Get all audio bot nodes",
            description = "Returns a list of all audio bot nodes. This list is not paginated.",
            tags = {"Audio Bot Node (Admin)"}
    )
    @GetMapping
    public
    ResponseEntity<DataResponse<List<AudioBotNodeListResponse>>> getAllAudioBotNodes() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                audioBotNodeService.getNodeList(),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Get audio bot node details",
            description = "Returns a single detailed response about node",
            tags = {"Audio Bot Node (Admin)"}
    )
    @GetMapping("/{nodeId}")
    public ResponseEntity<DataResponse<AudioBotNodeDetailResponse>> getAudioBotNodeDetail(@PathVariable Long nodeId) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                audioBotNodeService.getNodeDetail(nodeId),
                HttpStatus.OK
        );
    }


}
