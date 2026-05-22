package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.dns.DnsRecord;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DnsRecordRepository extends JpaSpecificationExecutor<DnsRecord>, JpaRepository<DnsRecord, Long> {
    Optional<DnsRecord> findByName(String name);
}
