package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.teaspeak.admin.QueryInstanceInitRequest;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.enums.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.QueryInstanceService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/admin/query-instances")
@RequiredArgsConstructor
public class QueryInstanceController {

    private final CurrentUser currentUser;
    private final QueryInstanceService queryInstanceService;
    private final MessageService messageService;

    public ResponseEntity<?> addQueryInstance(@Valid @RequestBody QueryInstanceInitRequest queryInitRequest) {
        queryInstanceService.initQueryInstance(queryInitRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.QUERY_INSTANCE_INITIATED),
                HttpStatus.OK
        );
    }

}
