package dev.parhamziaei.teahub.integration.zone_manager.liara;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.parhamziaei.teahub.entity.jpa.dns.*;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.enums.dns.DnsProviderStatus;
import dev.parhamziaei.teahub.enums.dns.DnsProviderType;
import dev.parhamziaei.teahub.enums.dns.DnsRecordType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
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

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class LiaraDnsProviderGateway implements DnsProviderGateway {

    private final LiaraDnsProviderRepository liaraDnsProviderRepo;
    private final DnsZoneRepository dnsZoneRepository;
    private final ADnsRecordRepository aDnsRecordRepository;
    private final ModelMapper modelMapper;
    private final SrvDnsRecordRepository srvDnsRecordRepository;
    private final DnsRecordRepository dnsRecordRepository;
    private final ObjectMapper objectMapper;

    @Override
    public DnsProviderType getType() {
        return DnsProviderType.LIARA;
    }

    private RestClient getRestClient() {
        LiaraDnsProvider liara = liaraDnsProviderRepo.find()
                .orElseThrow(DnsProviderNotConfiguredException::new);

        if (!liara.isActive())
            throw new DnsProviderNotConfiguredException();

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

    private <T, U> U executePost(
            String uri,
            T body,
            Class<U> responseType
    ) {
        try {

            log.debug("Trying to post request to {} body: {}", uri, new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(body));
            JsonNode root = getRestClient().post()
                    .uri(uri)
                    .body(body)
                    .retrieve()
                    .body(JsonNode.class);

            U response = objectMapper.treeToValue(root.get("data"), responseType);

            changeStatus(DnsProviderStatus.CONNECTED);
            return response;
        } catch (RestClientException | IOException e) {
            log.warn("Liara APIs throw an exception, failed to retrieve POST request", e);

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

    public boolean isSubdomainAvailable(String zoneName, String subdomain) {
        return getRecordList(zoneName)
                .stream()
                .noneMatch(r -> r.getName().equalsIgnoreCase(subdomain));
    }

    @Override
    @Transactional
    public void addSrvRecord(String zoneName, String subdomain, TeaSpeakResource resource) {
        final String ip = resource.getParentQueryInstance().getCredentials().ip();
        List<LiaraRecordDTO> records = getRecordList(zoneName);
        Optional<LiaraRecordDTO> optionalARecord = records.stream()
                .filter(r -> r.getType() == DnsRecordType.A && r.getContent().getIp().equals(ip))
                .findFirst();

        final String aRecordAddress;
        if (optionalARecord.isEmpty()) {
            String aRecordName = "node-" + resource.getParentQueryInstance().getId();
            addARecord(zoneName, aRecordName, ip);
            aRecordAddress = aRecordName + "." + zoneName;
        } else
            aRecordAddress = optionalARecord.get().getName();

        LiaraRecordDTO recordRequest = LiaraRecordDTO.builder()
                .name("_ts3._udp." + subdomain)
                .ttl(3600)
                .type(DnsRecordType.SRV)
                .contents(List.of(
                        LiaraRecordDTO.Content.builder()
                                .host(aRecordAddress)
                                .port(resource.getPort())
                                .weight(5)
                                .priority(1)
                                .build()
                )).build();

        LiaraRecordDTO liaraRecord = executePost(
                "/api/v1/zones/" + zoneName + "/dns-records",
                recordRequest,
                LiaraRecordDTO.class
        );

        SrvDnsRecord srvRecord = srvDnsRecordRepository.findByName(liaraRecord.getName())
                .orElse(modelMapper.map(liaraRecord, SrvDnsRecord.class));
        srvRecord.setHost(liaraRecord.getContent().getHost());
        srvRecord.setPort(liaraRecord.getContent().getPort());
        srvRecord.setPriority(liaraRecord.getContent().getPriority());
        srvRecord.setWeight(liaraRecord.getContent().getWeight());
        srvRecord.setDnsZone(dnsZoneRepository.findByName(zoneName).orElseThrow());
        srvRecord.setOwner(resource.getOwner());
        srvRecord.setTtl(liaraRecord.getTtl());
        srvRecord.setTargetResource(resource);
        srvRecord.setAssigned(true);
        srvDnsRecordRepository.save(srvRecord);
    }

    @Override
    public void deleteSrvRecord(DnsZone zone, String recordName) {

    }

    @Override
    @Transactional
    public void reassignAllUnassignedSrvRecords() {
        srvDnsRecordRepository.findAllByDnsZoneProviderTypeAndAssignedIsFalse(getType())
                .forEach(dbSrvRecord -> addSrvRecord(
                        dbSrvRecord.getDnsZone().getName(),
                        dbSrvRecord.getNameWithoutTs3Prefix(),
                        dbSrvRecord.getTargetResource())
                );
    }

    private void addARecord(String zoneName, String name, String ip) {
        LiaraRecordDTO aRecordRequest = LiaraRecordDTO.builder()
                .name(name)
                .type(DnsRecordType.A)
                .ttl(120)
                .contents(List.of(new LiaraRecordDTO.Content(ip)))
                .build();

        executePost(
                "/api/v1/zones/" + zoneName + "/dns-records",
                aRecordRequest,
                LiaraRecordDTO.class
        );
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
        List<SrvDnsRecord> dbSrvRecords = srvDnsRecordRepository.findAllByDnsZoneProviderType(getType());
        getRecordList(zone.getName()).forEach(liaraRecord -> {
            switch (liaraRecord.getType()) {
                case A -> {
                    ADnsRecord aRecord = aDnsRecordRepository.findByName(liaraRecord.getName())
                            .orElse(modelMapper.map(liaraRecord, ADnsRecord.class));

                    aRecord.setIp(liaraRecord.getContent().getIp());
                    aRecord.setDnsZone(zone);
                    aDnsRecordRepository.save(aRecord);
                }
                case SRV -> {
                    SrvDnsRecord srvRecord = srvDnsRecordRepository.findByName(liaraRecord.getName())
                            .orElse(modelMapper.map(liaraRecord, SrvDnsRecord.class));

                    srvRecord.setHost(liaraRecord.getContent().getHost());
                    srvRecord.setPort(liaraRecord.getContent().getPort());
                    srvRecord.setPriority(liaraRecord.getContent().getPriority());
                    srvRecord.setWeight(liaraRecord.getContent().getWeight());
                    srvRecord.setTtl(liaraRecord.getTtl());
                    srvRecord.setDnsZone(zone);
                    srvRecord.setAssigned(dbSrvRecords.stream().anyMatch(r -> r.getName().equals(liaraRecord.getName()) && r.hasTargetResource()));
                    srvDnsRecordRepository.save(srvRecord);
                }
            }
        });
    }

    @Override
    @Transactional
    public List<DnsRecord> getRecords(String zoneName) {
        syncRecords(dnsZoneRepository.findByName(zoneName).orElseThrow(NoSuchEntityException::new));
        return dnsRecordRepository.findAllByDnsZone_Name(zoneName);
    }

}
