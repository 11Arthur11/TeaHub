package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.dns.ADnsRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ADnsRecordRepository extends JpaRepository<ADnsRecord, Long> {
    Optional<ADnsRecord> findByName(String name);
}
