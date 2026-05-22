package dev.parhamziaei.teahub.repository.jpa.specification;

import dev.parhamziaei.teahub.entity.jpa.dns.DnsRecord;
import dev.parhamziaei.teahub.entity.jpa.dns.SrvDnsRecord;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.enums.dns.DnsRecordType;
import org.springframework.data.jpa.domain.Specification;

public class DnsRecordSpecification {

    public static Specification<DnsRecord> byType(DnsRecordType type) {
        return (root, query, cb) -> {
            if (type == null) return null;
            return cb.equal(root.get("recordType"), type);
        };
    }

    public static Specification<DnsRecord> byZoneName(String zoneName) {
        return (root, query, cb) -> {
            if (zoneName == null || zoneName.isEmpty()) return null;
            return cb.equal(root.get("dnsZone").get("name"), zoneName);
        };
    }

    public static Specification<SrvDnsRecord> byOwner(Long ownerId) {
        return (root, query, cb) -> {
            if (ownerId == null) return null;
            return cb.equal(root.get("owner").get("id"), ownerId);
        };
    }

}
