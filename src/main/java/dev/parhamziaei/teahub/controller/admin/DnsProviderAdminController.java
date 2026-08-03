package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.dns.admin.LiaraDnsProviderPersistRequest;
import dev.parhamziaei.teahub.dto.response.dns.admin.LiaraDnsProviderDetailResponse;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.DnsProviderService;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "DNS Provisioning (Admin) WORKING ON IT...")
@RestController
@RequestMapping("/v1/admin/dns")
@RequiredArgsConstructor
public class DnsProviderAdminController {

    private final DnsProviderService dnsProviderService;
    private final MessageService messageService;
    private final CurrentUser currentUser;

    @GetMapping("/liara")
    public ResponseEntity<DataResponse<LiaraDnsProviderDetailResponse>> getLiaraDnsProviders() {
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                dnsProviderService.getLiaraDnsProviderDetail(),
                HttpStatus.OK
        );
    }

    @PostMapping("/liara")
    public ResponseEntity<SimpleResponse> saveLiaraDnsProvider(@Valid @RequestBody LiaraDnsProviderPersistRequest dnsProviderPersistRequest) {
        dnsProviderService.persistLiaraDnsProvider(dnsProviderPersistRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

    @GetMapping("/records/{zoneName}")
    public ResponseEntity<?> zoneRecords(@PathVariable String zoneName) {
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                dnsProviderService.getRecords(zoneName),
                HttpStatus.OK
        );
    }

    @GetMapping("/records/resource/{resourceId}")
    public ResponseEntity<?> liaraZoneRecords(@PathVariable Long resourceId) {
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                dnsProviderService.getAssignedRecordByResource(resourceId),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/records/{recordId}")
    public ResponseEntity<SimpleResponse> unassignRecord(@PathVariable Long recordId) {
        dnsProviderService.deleteRecordById(currentUser.getId(), recordId);
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                messageService.get(ServiceMessage.DEFAULT_DELETED),
                HttpStatus.OK
        );
    }

    @PatchMapping("/zones/{zoneId}/toggle-active")
    public ResponseEntity<SimpleResponse> toggleZoneActive(@PathVariable Long zoneId) {
        dnsProviderService.toggleZoneActive(zoneId);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

    @PatchMapping("/records/{recordId}/re-assign")
    public ResponseEntity<SimpleResponse> reassignRecord(@PathVariable Long recordId) {
        //todo
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
                HttpStatus.OK
        );
    }

}
