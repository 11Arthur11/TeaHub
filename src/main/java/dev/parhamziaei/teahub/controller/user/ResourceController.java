package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.dto.request.resource.user.NewTeaSpeakResourceRequest;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.ResourceService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;
    private final CurrentUser currentUser;
    private final MessageService messageService;

    @PostMapping("/new")
    public ResponseEntity<SimpleResponse> newResource(@Valid @RequestBody AbstractNewResourceRequest newResourceRequest) {
        resourceService.newBillableResource(currentUser.getId(), newResourceRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.PROCESSING,
                messageService.get(ServiceMessage.RESOURCE_PROCESSING),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<?> getResources() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                resourceService.getAllUserResources(currentUser.getId()),
                HttpStatus.OK
        );
    }

    @GetMapping("/{resourceId}")
    public ResponseEntity<?> getResourceById(@PathVariable Long resourceId) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                resourceService.findResourceById(currentUser.getId(), resourceId),
                HttpStatus.OK
        );
    }

}
