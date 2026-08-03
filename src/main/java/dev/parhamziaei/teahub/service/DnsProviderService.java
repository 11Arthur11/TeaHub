package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.dns.admin.LiaraDnsProviderPersistRequest;
import dev.parhamziaei.teahub.dto.request.dns.user.AssignSubdomainRequest;
import dev.parhamziaei.teahub.dto.response.dns.admin.DnsZoneListResponse;
import dev.parhamziaei.teahub.dto.response.dns.admin.LiaraDnsProviderDetailResponse;
import dev.parhamziaei.teahub.dto.response.dns.admin.record.ADnsRecordResponse;
import dev.parhamziaei.teahub.dto.response.dns.admin.record.AbstractDnsRecordResponse;
import dev.parhamziaei.teahub.dto.response.dns.admin.record.SrvDnsRecordAdminResponse;
import dev.parhamziaei.teahub.dto.response.dns.user.DnsRecordUserResponse;
import dev.parhamziaei.teahub.dto.response.dns.user.ZoneUserResponse;
import dev.parhamziaei.teahub.entity.jpa.dns.DnsZone;
import dev.parhamziaei.teahub.entity.jpa.dns.LiaraDnsProvider;
import dev.parhamziaei.teahub.entity.jpa.dns.SrvDnsRecord;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.dns.DnsProviderType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.dns.DnsProviderNotConfiguredException;
import dev.parhamziaei.teahub.exception.custom.service.dns.ResourceAlreadyHasAssignedSubdomainException;
import dev.parhamziaei.teahub.exception.custom.service.dns.ZoneDisabledException;
import dev.parhamziaei.teahub.integration.zone_manager.component.DnsProviderRegistry;
import dev.parhamziaei.teahub.integration.zone_manager.component.DnsProviderGateway;
import dev.parhamziaei.teahub.repository.jpa.*;
import dev.parhamziaei.teahub.service.mapper.LiaraDnsProviderMapStruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DnsProviderService {

    private final LiaraDnsProviderRepository liaraDnsProviderRepo;
    private final LiaraDnsProviderMapStruct liaraDnsProviderMapStruct;
    private final BaseDnsProviderRepository baseDnsProviderRepo;
    private final DnsProviderRegistry providerRegistry;
    private final ModelMapper modelMapper;
    private final DnsZoneRepository dnsZoneRepository;
    private final SrvDnsRecordRepository srvDnsRecordRepository;
    private final TeaSpeakResourceRepository teaSpeakResourceRepository;
    private final UserRepository userRepository;

    private SrvDnsRecord loadSrvRecordByPermission(Long recordId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NoSuchEntityException::new);
        if (user.isAdmin())
            return srvDnsRecordRepository.findById(recordId)
                    .orElseThrow(NoSuchEntityException::new);
        else
            return srvDnsRecordRepository.findOneByIdAndOwnerId(recordId, userId)
                    .orElseThrow(NoSuchEntityException::new);
    }

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

        List<DnsZoneListResponse> dnsZoneListResponse = new ArrayList<>();

        provider.getDnsZones().forEach(zone -> {
            DnsZoneListResponse zoneResponse = modelMapper.map(zone, DnsZoneListResponse.class);
            dnsZoneListResponse.add(zoneResponse);
        });

        response.setDnsZones(dnsZoneListResponse);
        return response;
    }

    public List<AbstractDnsRecordResponse> getRecords(String zoneName) {
        DnsZone zone = dnsZoneRepository.findByName(zoneName)
                .orElseThrow(NoSuchEntityException::new);

        return providerRegistry.getProvider(zone.getProvider().getType())
                .getRecords(zoneName)
                .stream()
                .map(r -> switch (r.getRecordType()) {
                    case SRV -> modelMapper.map(r, SrvDnsRecordAdminResponse.class);
                    case A -> modelMapper.map(r, ADnsRecordResponse.class);
                }).toList();
    }

    public boolean isSubdomainAvailable(Long zoneId, String subdomain) {
        DnsZone zone = dnsZoneRepository.findById(zoneId)
                .orElseThrow(NoSuchEntityException::new);
        
        return providerRegistry.getProvider(zone.getProvider().getType())
                .isSubdomainAvailable(zone.getName(), subdomain);
    }

    public List<ZoneUserResponse> getAvailableZones() {
        List<ZoneUserResponse> res = dnsZoneRepository.findAll().stream()
                .filter(DnsZone::isActive)
                .map(dnsZone -> modelMapper.map(dnsZone, ZoneUserResponse.class))
                .toList();

        if (res.isEmpty())
            throw new NoSuchDataException();

        return res;
    }

    public void updateZones(DnsProviderType providerType) {
        providerRegistry.getProvider(providerType)
                .syncZones();
    }

    @Scheduled(cron = "0/10 * * * * *")
    public void updateAllZones() {
        if (baseDnsProviderRepo.count() == 0)
            return;
        log.debug("Syncing all dns providers...");
        try {
            providerRegistry.executeToAll(DnsProviderGateway::syncZones);
            log.debug("All providers sync complete.");
        } catch (Exception e) {
            log.warn("There was a problem while syncing all dns zones: {}", e.getMessage());
        }
    }

    @Transactional
    public List<DnsRecordUserResponse> getRecordsByUser(Long userId) {
        return srvDnsRecordRepository.findByOwnerId(userId)
                .stream()
                .map(r ->
                        DnsRecordUserResponse.builder()
                                .id(r.getId())
                                .assignedToResourceId(r.getTargetResource().getId())
                                .value(r.getName().replace("_ts3._udp.", ""))
                                .zone(new ZoneUserResponse(r.getDnsZone().getId(), r.getDnsZone().getName()))
                                .build()
                ).toList();
    }

    @Transactional
    public DnsRecordUserResponse getAssignedRecordByResource(Long userId, Long resourceId) {
        SrvDnsRecord record = srvDnsRecordRepository.findByTargetResourceIdAndOwnerId(resourceId, userId)
                .orElseThrow(NoSuchDataException::new);

        return DnsRecordUserResponse.builder()
                .id(record.getId())
                .assignedToResourceId(resourceId)
                .value(record.getName().replace("_ts3._udp.", ""))
                .zone(new ZoneUserResponse(record.getDnsZone().getId(), record.getDnsZone().getName()))
                .build();
    }

    @Transactional
    public SrvDnsRecordAdminResponse getAssignedRecordByResource(Long resourceId) {
        SrvDnsRecord record = srvDnsRecordRepository.findByTargetResourceId(resourceId)
                .orElseThrow(NoSuchDataException::new);

        return modelMapper.map(record, SrvDnsRecordAdminResponse.class);
    }

    public void deleteRecordById(Long userId, Long recordId) {
        SrvDnsRecord srvDnsRecord = loadSrvRecordByPermission(recordId, userId);

        DnsZone zone = srvDnsRecord.getDnsZone();
        providerRegistry.getProvider(zone.getProvider().getType())
                .deleteSrvRecord(zone, srvDnsRecord.getName());
        srvDnsRecordRepository.delete(srvDnsRecord);
    }

    public void toggleZoneActive(Long zoneId) {
        DnsZone zone = dnsZoneRepository.findById(zoneId)
                .orElseThrow(NoSuchEntityException::new);
        zone.setActive(!zone.isActive());
        dnsZoneRepository.save(zone);
    }

    public void deleteAssignedRecordByResource(Long resourceId) {
        Optional<SrvDnsRecord> record = srvDnsRecordRepository.findByTargetResourceId(resourceId);
        record.ifPresent(srvDnsRecord -> {
            providerRegistry.getProvider(srvDnsRecord.getDnsZone().getProvider().getType())
                    .deleteSrvRecord(srvDnsRecord.getDnsZone(), srvDnsRecord.getName());
            srvDnsRecordRepository.delete(srvDnsRecord);
        });
    }

    @Async
    public void reassignAllUnassignedRecords() {
        providerRegistry.executeToAll(DnsProviderGateway::reassignAllUnassignedSrvRecords);
    }

    public void assignSubdomain(Long userId, AssignSubdomainRequest request) {
        if (srvDnsRecordRepository.existsByTargetResourceId(request.getTeaSpeakResourceId()))
            throw new ResourceAlreadyHasAssignedSubdomainException();

        DnsZone zone = dnsZoneRepository.findById(request.getZoneId())
                .orElseThrow(NoSuchEntityException::new);

        if (!zone.isActive())
            throw new ZoneDisabledException(zone.getName());

        TeaSpeakResource resource = teaSpeakResourceRepository.findOneByOwnerId(userId, request.getTeaSpeakResourceId())
                .orElseThrow(NoSuchDataException::new);

        providerRegistry.getProvider(zone.getProvider().getType())
                .addSrvRecord(
                        zone.getName(),
                        request.getSubdomain(),
                        resource
                );
    }

}
