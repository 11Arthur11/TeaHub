package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.dns.user.AssignSubdomainRequest;
import dev.parhamziaei.teahub.dto.response.dns.user.DnsRecordUserResponse;
import dev.parhamziaei.teahub.dto.response.dns.user.ZoneUserResponse;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.DnsProviderService;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import dev.parhamziaei.teahub.validation.annotation.Subdomain;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/dns")
@RequiredArgsConstructor
public class DnsController {

    private final DnsProviderService dnsProviderService;
    private final CurrentUser currentUser;
    private final MessageService messageService;

    @RequestMapping(value = "/zones/{zoneId}/subdomains/{subdomain}/availability", method = RequestMethod.HEAD)
    public ResponseEntity<Void> isAvailable(@PathVariable Long zoneId, @PathVariable @Subdomain String subdomain) {
        return dnsProviderService.isSubdomainAvailable(zoneId, subdomain)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping("/zones")
    public ResponseEntity<DataResponse<List<ZoneUserResponse>>> getAvailableZones() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                dnsProviderService.getAvailableZones(),
                HttpStatus.OK
        );
    }

    @GetMapping("/records/{resourceId}")
    public ResponseEntity<DataResponse<DnsRecordUserResponse>> getAssignedRecord(@PathVariable Long resourceId) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                dnsProviderService.getAssignedRecordByResource(currentUser.getId(), resourceId),
                HttpStatus.OK
        );
    }

    @GetMapping("/records")
    public ResponseEntity<DataResponse<List<DnsRecordUserResponse>>> getAssignedRecords() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                dnsProviderService.getRecordsByUser(currentUser.getId()),
                HttpStatus.OK
        );
    }

    @PostMapping("/records")
    public ResponseEntity<SimpleResponse> assignSubdomain(@RequestBody @Valid AssignSubdomainRequest request) {
        dnsProviderService.assignSubdomain(currentUser.getId(), request);
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                messageService.get(ServiceMessage.DEFAULT_ACTION_DONE),
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

}
