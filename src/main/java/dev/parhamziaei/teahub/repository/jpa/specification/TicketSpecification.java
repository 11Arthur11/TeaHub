package dev.parhamziaei.teahub.repository.jpa.specification;

import dev.parhamziaei.teahub.entity.jpa.ticket.Ticket;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.ticket.TicketDepartment;
import dev.parhamziaei.teahub.enums.ticket.TicketStatus;
import org.springframework.data.jpa.domain.Specification;

public class TicketSpecification {

    public static Specification<Ticket> mustHaveAccess(Long userId) {
        return (root, query, cb) -> {
            return cb.equal(root.get("owner").get("id"), userId);
        };
    }

    public static Specification<Ticket> hasStatus(TicketStatus status) {
        return (root, query, cb) -> {
            if (status == null) return null;
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Ticket> hasDepartment(TicketDepartment department) {
        return (root, query, cb) -> {
            if (department == null) return null;
            return cb.equal(root.get("department"), department);
        };
    }

}
