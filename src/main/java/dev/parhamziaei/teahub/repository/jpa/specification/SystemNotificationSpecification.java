package dev.parhamziaei.teahub.repository.jpa.specification;

import dev.parhamziaei.teahub.entity.jpa.system.SystemNotification;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class SystemNotificationSpecification {

    public static Specification<SystemNotification> byExpired() {
        return (root, query, cb) ->
            cb.lessThanOrEqualTo(root.get("expiresAt"), LocalDateTime.now());
    }

}
