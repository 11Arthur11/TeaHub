package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.dns.SrvDnsRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SrvDnsRecordRepository extends JpaRepository<SrvDnsRecord, Long> {
    Optional<SrvDnsRecord> findByName(String name);
}
