package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.teaspeak.admin.QueryInstanceEditRequest;
import dev.parhamziaei.teahub.dto.request.teaspeak.admin.QueryInstanceInitRequest;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.QueryInstanceService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/query-instances")
@RequiredArgsConstructor
public class QueryInstanceController {

    private final QueryInstanceService queryInstanceService;
    private final MessageService messageService;

    @Operation(
            summary = "Get all query instances",
            description = "Returns a list of all query instances. This list is not paginated.",
            tags = {"Query Instance"}
    )
    @GetMapping
    public ResponseEntity<?> getAllQueryInstance() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                queryInstanceService.getAllQueryInstance(),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Initiate a query instance",
            description = "Creates and initiates a new query instance based on the provided configuration.",
            tags = {"Query Instance"}
    )
    @PostMapping("/initiate")
    public ResponseEntity<SimpleResponse> initQueryInstance(@Valid @RequestBody QueryInstanceInitRequest queryInitRequest) {
        queryInstanceService.initiateQueryInstance(queryInitRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.QUERY_INSTANCE_INITIATED),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "edit an existing query instance",
            description = "edit query instance, if base credentials was edited, query will be re-dispatched.",
            tags = {"Query Instance"}
    )
    @PutMapping("/{id}")
    public ResponseEntity<SimpleResponse> editQueryInstance(@PathVariable Long id, @RequestBody QueryInstanceEditRequest editRequest) {
        queryInstanceService.editQueryInstance(id, editRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Enable query instance",
            description = "Enables and dispatches the specified query instance. The operation is processed asynchronously.",
            tags = {"Query Instance"}
    )
    @PatchMapping("/{id}/enable")
    public ResponseEntity<SimpleResponse> enableQueryInstance(@PathVariable Long id) {
        queryInstanceService.dispatchQueryInstance(id);
        return ResponseBuilder.buildSuccess(
                ResponseType.PROCESSING,
                messageService.get(ServiceMessage.QUERY_INSTANCE_ENABLING),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Disable query instance",
            description = "Disables the specified query instance. The operation is processed asynchronously.",
            tags = {"Query Instance"}
    )
    @PatchMapping("/{id}/disable")
    public ResponseEntity<SimpleResponse> disableQueryInstance(@PathVariable Long id) {
        queryInstanceService.disableQueryInstance(id);
        return ResponseBuilder.buildSuccess(
                ResponseType.PROCESSING,
                messageService.get(ServiceMessage.QUERY_INSTANCE_DISABLING),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Remove query instance",
            description = "Permanently removes the specified query instance and remove it from wep application lifecycle",
            tags = {"Query Instance"}
    )
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<SimpleResponse> removeQueryInstance(@PathVariable Long id) {
        queryInstanceService.removeQueryInstance(id);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.QUERY_INSTANCE_DELETED),
                HttpStatus.OK
        );
    }



}
