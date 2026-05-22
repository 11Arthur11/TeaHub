package dev.parhamziaei.teahub.integration.zone_manager.component;

import dev.parhamziaei.teahub.entity.jpa.dns.DnsZone;
import dev.parhamziaei.teahub.enums.dns.DnsProviderType;

public interface DnsProviderGateway {

    DnsProviderType getType();
    void syncZones();
    void syncRecords(DnsZone zone);
}
