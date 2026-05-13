package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.dto.request.query.ResourceFilterRequest;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.resource.AbstractResourceDetailResponse;
import dev.parhamziaei.teahub.dto.response.resource.ResourceListAdminResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.service.ResourceService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/resources")
@RequiredArgsConstructor
public class ResourceAdminController {

    private final ResourceService resourceService;

    @Operation(
            summary = "Get all resources for admin",
            description = "Returns a list of all resources. Note: the list is paginated. " +
                    "You can apply filters via query parameters in ResourceFilterRequest.",
            tags = {"Resource (Admin)"}
    )
    @PostMapping
    public ResponseEntity<DataResponse<PagedModel<ResourceListAdminResponse>>> getAllResources(@RequestBody ResourceFilterRequest filter) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                resourceService.getAllResources(filter),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Get resource details for admin",
            description = "Returns detailed information for a single resource identified by its ID.",
            tags = {"Resource (Admin)"}
    )
    @GetMapping("/{resourceId}")
    public ResponseEntity<DataResponse<AbstractResourceDetailResponse>> getResource(@PathVariable Long resourceId) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                resourceService.findResource(resourceId),
                HttpStatus.OK
        );
    }

}
