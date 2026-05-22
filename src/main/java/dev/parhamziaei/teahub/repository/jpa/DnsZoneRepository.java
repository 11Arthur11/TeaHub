package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.dns.DnsZone;
import dev.parhamziaei.teahub.enums.dns.DnsProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DnsZoneRepository extends JpaRepository<DnsZone, Long> {

    List<DnsZone> findByProviderType(DnsProviderType providerType);

    Optional<DnsZone> findByName(String name);
}
