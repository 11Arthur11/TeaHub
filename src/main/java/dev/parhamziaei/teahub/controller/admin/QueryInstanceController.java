package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.teaspeak.admin.QueryInstanceInitRequest;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.QueryInstanceService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/query-instances")
@RequiredArgsConstructor
public class QueryInstanceController {

    private final CurrentUser currentUser;
    private final QueryInstanceService queryInstanceService;
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<?> getAllQueryInstance() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                queryInstanceService.getAllQueryInstance(),
                HttpStatus.OK
        );
    }

    @PostMapping("/initiate")
    public ResponseEntity<SimpleResponse> initQueryInstance(@Valid @RequestBody QueryInstanceInitRequest queryInitRequest) {
        queryInstanceService.initiateQueryInstance(queryInitRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.QUERY_INSTANCE_INITIATED),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<SimpleResponse> enableQueryInstance(@PathVariable Long id) {
        queryInstanceService.dispatchQueryInstance(id);
        return ResponseBuilder.buildSuccess(
                ResponseType.PROCESSING,
                messageService.get(ServiceMessage.QUERY_INSTANCE_ENABLING),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<SimpleResponse> disableQueryInstance(@PathVariable Long id) {
        queryInstanceService.disableQueryInstance(id);
        return ResponseBuilder.buildSuccess(
                ResponseType.PROCESSING,
                messageService.get(ServiceMessage.QUERY_INSTANCE_DISABLING),
                HttpStatus.OK
        );
    }

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
