package dev.parhamziaei.teahub.integration.ippanel;

import dev.parhamziaei.teahub.configuration.properties.IPPanelProperties;
import dev.parhamziaei.teahub.integration.ippanel.dto.request.PatternMessageRequest;
import dev.parhamziaei.teahub.integration.ippanel.dto.response.PatternMessageResponse;
import dev.parhamziaei.teahub.utils.PhoneNumbers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class IPPanelService {

    private final IPPanelProperties ipPanelProperties;
    private final RestClient restClient;

    public IPPanelService(IPPanelProperties ipPanelProperties) {
        this.ipPanelProperties = ipPanelProperties;
        this.restClient = RestClient.builder()
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
                    httpHeaders.add("Authorization", this.ipPanelProperties.apiKey());
                    httpHeaders.add(HttpHeaders.ACCEPT_CHARSET, "utf-8");
                })
                .baseUrl(this.ipPanelProperties.baseUrl())
                .build();
    }

    @Async
    public void sendTwoFactorSMS(String code, String toNumber) {
        if (PhoneNumbers.isFormatted.test(toNumber)) {
            toNumber = PhoneNumbers.formatedOf(toNumber);
        }
        Map<String, String> params = new HashMap<>();
        List<String> recipients = List.of(toNumber);
        params.put("code", code);
        PatternMessageRequest patternMessageRequest = PatternMessageRequest.builder()
                .sending_type("pattern")
                .from_number(ipPanelProperties.fromNumber())
                .code(ipPanelProperties.twoFactoMessagePatternCode())
                .params(params)
                .recipients(recipients)
                .build();

        try {
            ResponseEntity<PatternMessageResponse> response = restClient.post()
                    .uri(this.ipPanelProperties.baseUrl() + "/api/send")
                    .body(patternMessageRequest)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(PatternMessageResponse.class);

            if (response.getBody() != null)
                log.info("SMS sent success: {}", response.getBody().getMeta().isOk());
        } catch (RestClientException e) {
            // todo make this log the exception message
            log.error("SMS sending error", e.getMessage());
        }

    }

}
