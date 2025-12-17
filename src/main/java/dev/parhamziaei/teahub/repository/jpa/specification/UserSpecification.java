package dev.parhamziaei.teahub.repository.jpa.specification;

import dev.parhamziaei.teahub.entity.jpa.user.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> byRoleId(Long roleId) {
        return (root, query, cb) -> {
            if (roleId == null) return null;
            return cb.equal(root.get("role").get("id"), roleId);
        };
    }

    public static Specification<User> byEnabled(Boolean enabled) {
        return (root, query, cb) -> {
            if (enabled == null) return null;
            return cb.equal(root.get("enabled"), enabled);
        };
    }

    public static Specification<User> byLocked(Boolean locked) {
        return (root, query, cb) -> {
            if (locked == null) return null;
            return cb.equal(root.get("locked"), locked);
        };
    }

}
