package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.dns.BaseDnsProvider;
import dev.parhamziaei.teahub.enums.dns.DnsProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaseDnsProviderRepository extends JpaRepository<BaseDnsProvider, Long> {

    Optional<BaseDnsProvider> findByType(DnsProviderType type);

}
