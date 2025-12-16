package dev.parhamziaei.teahub.repository.jpa.specification;

import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.enums.shop.ProductPeriod;
import dev.parhamziaei.teahub.enums.shop.ResourceStatus;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import org.springframework.data.jpa.domain.Specification;

public class BillableResourceSpecification {

    public static Specification<BillableResource> byOwnerPhone(String ownerPhone) {
        return (root, query, cb) -> {
            if (ownerPhone == null) return null;
            return cb.equal(root.get("owner").get("phone"), ownerPhone);
        };
    }

    public static Specification<BillableResource> byStatus(ResourceStatus resourceStatus) {
        return (root, query, cb) -> {
            if (resourceStatus == null) return null;
            return cb.equal(root.get("resourceStatus"), resourceStatus);
        };
    }

    public static Specification<BillableResource> byType(ResourceType type) {
        return (root, query, cb) -> {
            if (type == null) return null;
            return cb.equal(root.get("resourceType"), type);
        };
    }

    public static Specification<BillableResource> forUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("owner").get("id"), userId);
    }

}
