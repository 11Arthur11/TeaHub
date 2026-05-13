package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.dto.request.resource.user.BillableResourceEditRequest;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.dto.response.resource.AbstractResourceDetailResponse;
import dev.parhamziaei.teahub.dto.response.resource.ResourceListResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.ResourceService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/services")
@RequiredArgsConstructor
public class ResourceServiceController {

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
    public ResponseEntity<DataResponse<List<ResourceListResponse>>> getResources() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                resourceService.getAllUserResources(currentUser.getId()),
                HttpStatus.OK
        );
    }

    @GetMapping("/{resourceId}")
    public ResponseEntity<DataResponse<AbstractResourceDetailResponse>> getResourceById(@PathVariable Long resourceId) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                resourceService.findResourceByUser(currentUser.getId(), resourceId),
                HttpStatus.OK
        );
    }

    @PostMapping("/{resourceId}/prolong")
    public ResponseEntity<SimpleResponse> prolongResource(@PathVariable Long resourceId) {
        resourceService.prolongResource(currentUser.getId(), resourceId);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.RESOURCE_PROLONGED),
                HttpStatus.OK
        );
    }

    @PostMapping("/{resourceId}/edit")
    public ResponseEntity<SimpleResponse> prolongResource(@PathVariable Long resourceId, @RequestBody BillableResourceEditRequest editRequest) {
        resourceService.editResource(currentUser.getId(), resourceId, editRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.RESOURCE_EDITED),
                HttpStatus.OK
        );
    }

}
