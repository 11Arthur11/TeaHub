package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.dns.SrvDnsRecord;
import dev.parhamziaei.teahub.enums.dns.DnsProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SrvDnsRecordRepository extends JpaRepository<SrvDnsRecord, Long> {
    Optional<SrvDnsRecord> findByName(String name);

    @Query("SELECT sdr FROM SrvDnsRecord sdr WHERE sdr.targetResource.id = :resourceId AND sdr.targetResource.owner.id = :ownerId")
    Optional<SrvDnsRecord> findByTargetResourceIdAndOwnerId(@Param("resourceId") Long targetResourceId, @Param("ownerId") Long ownerId);

    @Query("SELECT sdr FROM SrvDnsRecord sdr WHERE sdr.owner.id = :ownerId")
    List<SrvDnsRecord> findByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT sdr FROM SrvDnsRecord sdr WHERE sdr.id = :id AND sdr.targetResource.owner.id = :ownerId")
    Optional<SrvDnsRecord> findOneByIdAndOwnerId(@Param("id") Long id, @Param("ownerId") Long ownerId);

    boolean existsByTargetResourceId(Long targetResourceId);

    List<SrvDnsRecord> findAllByDnsZoneProviderTypeAndAssignedIsFalse(DnsProviderType dnsZoneProviderType);

    List<SrvDnsRecord> findAllByDnsZoneProviderType(DnsProviderType dnsZoneProviderType);

    Optional<SrvDnsRecord> findByTargetResourceId(Long targetResourceId);
}
