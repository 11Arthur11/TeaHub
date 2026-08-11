package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.dto.request.system.ApplicationSettingDto;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.DetailedDataResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.service.AppSettingsService;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/app-settings")
public class ApplicationSettingsController {

    private final AppSettingsService appSettingsService;
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<DataResponse<ApplicationSettingDto>> getSettings() {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                appSettingsService.get(),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<DetailedDataResponse<ApplicationSettingDto>> setSettings(@RequestBody ApplicationSettingDto request) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                messageService.get(ServiceMessage.DEFAULT_EDITED),
                appSettingsService.save(request),
                HttpStatus.OK
        );

    }

}
