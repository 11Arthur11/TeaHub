package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.dns.admin.LiaraDnsProviderPersistRequest;
import dev.parhamziaei.teahub.dto.response.dns.admin.DnsZoneListResponse;
import dev.parhamziaei.teahub.dto.response.dns.admin.LiaraDnsProviderDetailResponse;
import dev.parhamziaei.teahub.entity.jpa.dns.BaseDnsProvider;
import dev.parhamziaei.teahub.entity.jpa.dns.LiaraDnsProvider;
import dev.parhamziaei.teahub.enums.dns.DnsProviderType;
import dev.parhamziaei.teahub.exception.custom.service.dns.DnsProviderNotConfiguredException;
import dev.parhamziaei.teahub.integration.zone_manager.component.DnsProviderFactory;
import dev.parhamziaei.teahub.integration.zone_manager.component.DnsProviderGateway;
import dev.parhamziaei.teahub.repository.jpa.BaseDnsProviderRepository;
import dev.parhamziaei.teahub.repository.jpa.LiaraDnsProviderRepository;
import dev.parhamziaei.teahub.service.mapper.LiaraDnsProviderMapStruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DnsProviderService {

    private final LiaraDnsProviderRepository liaraDnsProviderRepo;
    private final LiaraDnsProviderMapStruct liaraDnsProviderMapStruct;
    private final BaseDnsProviderRepository baseDnsProviderRepo;
    private final DnsProviderFactory providerFactory;
    private final ModelMapper modelMapper;
    private final MessageService messageService;

    @Async
    public void persistLiaraDnsProvider(LiaraDnsProviderPersistRequest persistRequest) {
        LiaraDnsProvider liaraDnsProvider = liaraDnsProviderRepo.find()
                .orElse(new LiaraDnsProvider());
        liaraDnsProviderMapStruct.toEntity(persistRequest, liaraDnsProvider);
        liaraDnsProviderRepo.save(liaraDnsProvider);
        updateZones(DnsProviderType.LIARA);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public LiaraDnsProviderDetailResponse getLiaraDnsProviderDetail() {
        LiaraDnsProvider provider = liaraDnsProviderRepo.find()
                .orElseThrow(DnsProviderNotConfiguredException::new);

        LiaraDnsProviderDetailResponse response = modelMapper.map(provider, LiaraDnsProviderDetailResponse.class);
        response.setStatus(messageService.get(provider.getStatus()));

        List<DnsZoneListResponse> dnsZoneListResponse = new ArrayList<>();

        provider.getDnsZones().forEach(zone -> {
            DnsZoneListResponse zoneResponse = modelMapper.map(zone, DnsZoneListResponse.class);
            zoneResponse.setStatus(messageService.get(zone.getStatus()));
            dnsZoneListResponse.add(zoneResponse);
        });

        response.setDnsZones(dnsZoneListResponse);
        return response;
    }

    public void updateZones(DnsProviderType providerType) {
        providerFactory.getProvider(providerType)
                .syncZones();
    }

    @Scheduled(cron = "0/10 * * * * *")
    public void updateAllZones() {
        if (baseDnsProviderRepo.count() == 0)
            return;
        log.debug("Syncing all dns zones...");
        try {
            providerFactory.executeToAll(DnsProviderGateway::syncZones);
        } catch (Exception e) {
            log.warn("There was a problem while syncing all dns zones: {}", e.getMessage());
        }
    }

}
