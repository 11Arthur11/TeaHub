package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.dto.request.query.ResourceFilterRequest;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.service.ResourceService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/resources")
@RequiredArgsConstructor
public class ResourceAdminController {

    private final ResourceService resourceService;

    @GetMapping
    public ResponseEntity<?> getAllResources(@RequestParam ResourceFilterRequest filter) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                resourceService.getAllResources(filter),
                HttpStatus.OK
        );
    }

    @GetMapping("/{resourceId}")
    public ResponseEntity<?> getResource(@PathVariable Long resourceId) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                resourceService.findResource(resourceId),
                HttpStatus.OK
        );
    }

}
