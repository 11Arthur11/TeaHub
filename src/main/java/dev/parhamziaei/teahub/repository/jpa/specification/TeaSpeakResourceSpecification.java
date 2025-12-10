package dev.parhamziaei.teahub.repository.jpa.specification;

import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.entity.jpa.user.Wallet;
import org.springframework.data.jpa.domain.Specification;

public class TeaSpeakResourceSpecification {

    public static Specification<TeaSpeakResource> forUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("owner").get("id"), userId);
    }

}
