package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.dto.request.dns.admin.LiaraDnsProviderPersistRequest;
import dev.parhamziaei.teahub.dto.request.query.DnsRecordFilterRequest;
import dev.parhamziaei.teahub.dto.response.dns.admin.LiaraDnsProviderDetailResponse;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.dns.DnsProviderType;
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

//    @GetMapping("/records")
//    public ResponseEntity<?> liaraZoneRecords(@ModelAttribute DnsRecordFilterRequest filterRequest) {
//
//    }
//
//    @PatchMapping("/liara/{zoneName}/active")
//    public ResponseEntity<?> liaraZoneRecords() {
//
//    }
//
//    @GetMapping("/liara/{zoneName}/deActive")
//    public ResponseEntity<?> liaraZoneRecords() {
//
//    }

}
