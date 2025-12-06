package dev.parhamziaei.teahub.repository.jpa.specification;

import dev.parhamziaei.teahub.entity.jpa.payment.Invoice;
import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;
import org.springframework.data.jpa.domain.Specification;

public class TeaSpeakProductSpecification {

    public static Specification<TeaSpeakProduct> isEnabled(boolean enabled) {
        return (root, query, cb) -> cb.equal(root.get("enabled"), enabled);
    }

}
