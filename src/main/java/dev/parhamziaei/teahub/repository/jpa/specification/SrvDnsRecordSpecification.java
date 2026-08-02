package dev.parhamziaei.teahub.repository.jpa.specification;

import dev.parhamziaei.teahub.entity.jpa.dns.SrvDnsRecord;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import org.springframework.data.jpa.domain.Specification;

public class SrvDnsRecordSpecification {

    public static Specification<SrvDnsRecord> mustHaveAccess(User user) {
        if (user.isAdmin())
            return null;
        return (root, query, cb) -> cb.equal(root.get("owner").get("id"), user.getId());
    }

    public static Specification<SrvDnsRecord> byResourceId(Long resourceId) {
        return (root, query, cb) -> cb.equal(root.get("targetResource").get("id"), resourceId);
    }

}
