package dev.parhamziaei.teahub.integration.zone_manager.component;

import dev.parhamziaei.teahub.entity.jpa.dns.DnsRecord;
import dev.parhamziaei.teahub.entity.jpa.dns.DnsZone;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.enums.dns.DnsProviderType;

import java.util.List;

public interface DnsProviderGateway {

    DnsProviderType getType();
    void syncZones();
    void syncRecords(DnsZone zone);
    List<? extends DnsRecord> getRecords(String zoneName);
    boolean isSubdomainAvailable(String zoneName, String subdomain);
    void addSrvRecord(String zoneName, String subdomain, TeaSpeakResource resource);
    void deleteSrvRecord(String recordName);
    void reassignAllUnassignedSrvRecords();

}
