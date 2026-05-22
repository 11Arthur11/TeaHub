package dev.parhamziaei.teahub.integration.zone_manager.liara;

import dev.parhamziaei.teahub.entity.jpa.dns.*;
import dev.parhamziaei.teahub.enums.dns.DnsProviderStatus;
import dev.parhamziaei.teahub.enums.dns.DnsProviderType;
import dev.parhamziaei.teahub.exception.custom.service.dns.DnsProviderApiException;
import dev.parhamziaei.teahub.exception.custom.service.dns.DnsProviderNotConfiguredException;
import dev.parhamziaei.teahub.integration.zone_manager.component.DnsProviderGateway;
import dev.parhamziaei.teahub.integration.zone_manager.liara.dto.BaseLiaraResponse;
import dev.parhamziaei.teahub.integration.zone_manager.liara.dto.LiaraRecordDTO;
import dev.parhamziaei.teahub.integration.zone_manager.liara.dto.LiaraZoneListResponse;
import dev.parhamziaei.teahub.repository.jpa.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class LiaraDnsProviderGateway implements DnsProviderGateway {

    private final LiaraDnsProviderRepository liaraDnsProviderRepo;
    private final DnsZoneRepository dnsZoneRepository;
    private final ADnsRecordRepository aDnsRecordRepository;
    private final ModelMapper modelMapper;
    private final SrvDnsRecordRepository srvDnsRecordRepository;

    @Override
    public DnsProviderType getType() {
        return DnsProviderType.LIARA;
    }

    private RestClient getRestClient() {
        LiaraDnsProvider liara = liaraDnsProviderRepo.find()
                .orElseThrow(DnsProviderNotConfiguredException::new);

        return RestClient.builder()
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
                    httpHeaders.add(HttpHeaders.ACCEPT_CHARSET, "utf-8");
                    httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + liara.getApiKey());
                })
                .baseUrl(liara.getBaseUrl())
                .build();
    }

    private LiaraDnsProvider loadProvider() {
        return liaraDnsProviderRepo.find()
                .orElseThrow(DnsProviderNotConfiguredException::new);
    }

    private void changeStatus(DnsProviderStatus status) {
        LiaraDnsProvider liara = liaraDnsProviderRepo.find()
                .orElseThrow(DnsProviderNotConfiguredException::new);
        liara.setStatus(status);
        liaraDnsProviderRepo.save(liara);
    }

    private List<LiaraZoneListResponse> getZoneList() {
        BaseLiaraResponse<List<LiaraZoneListResponse>> response = executeGet(
                "/api/v1/zones",
                new ParameterizedTypeReference<>() {}
        );
        if (response.getStatus().equals("success"))
            return response.getData();
        else
            throw new DnsProviderApiException();
    }

    private <T> T executeGet(
            String uri,
            ParameterizedTypeReference<T> responseType
    ) {
        try {
            ResponseEntity<T> response = getRestClient().get()
                    .uri(uri)
                    .retrieve()
                    .toEntity(responseType);

            changeStatus(DnsProviderStatus.CONNECTED);
            return response.getBody();
        } catch (RestClientException e) {
            log.warn("Liara APIs throw an exception, failed to retrieve GET request", e);

            if (e instanceof HttpClientErrorException.Unauthorized)
                changeStatus(DnsProviderStatus.API_KEY_REJECTED);
            else if (e instanceof HttpServerErrorException)
                changeStatus(DnsProviderStatus.SERVER_ERROR);
            else
                changeStatus(DnsProviderStatus.UNKNOWN);

            throw new DnsProviderApiException();
        }
    }

    private List<LiaraRecordDTO> getRecordList(String zoneName) {
        BaseLiaraResponse<List<LiaraRecordDTO>> response = executeGet(
                "/api/v1/zones/" + zoneName + "/dns-records",
                new ParameterizedTypeReference<>() {}
        );

        if (response.getStatus().equals("success"))
            return response.getData();
        else
            throw new DnsProviderApiException();
    }

    @Override
    @Transactional
    public void syncZones() {
        getZoneList().forEach(liaraZone -> {
            DnsZone internalZone = dnsZoneRepository.findByName(liaraZone.getName())
                    .orElse(new DnsZone(liaraZone.getName(), liaraZone.getStatus(), loadProvider()));

            dnsZoneRepository.save(internalZone);
            syncRecords(internalZone);
        });
    }

    @Override
    @Transactional
    public void syncRecords(DnsZone zone) {
        getRecordList(zone.getName()).forEach(liaraRecord -> {
            switch (liaraRecord.getType()) {
                case A -> {
                    ADnsRecord aRecord = aDnsRecordRepository.findByName(liaraRecord.getName())
                            .orElse(modelMapper.map(liaraRecord, ADnsRecord.class));

                    aRecord.setIp(liaraRecord.getContents().getIp());
                    aRecord.setDnsZone(zone);
                    aDnsRecordRepository.save(aRecord);
                }
                case SRV -> {
                    SrvDnsRecord srvRecord = srvDnsRecordRepository.findByName(liaraRecord.getName())
                            .orElse(modelMapper.map(liaraRecord, SrvDnsRecord.class));

                    srvRecord.setHost(liaraRecord.getContents().getHost());
                    srvRecord.setPort(liaraRecord.getContents().getPort());
                    srvRecord.setPriority(liaraRecord.getContents().getPriority());
                    srvRecord.setWeight(liaraRecord.getContents().getWeight());
                    srvRecord.setDnsZone(zone);
                    srvDnsRecordRepository.save(srvRecord);
                }
            }
        });
    }

}
