package dev.parhamziaei.teahub.repository.jpa.specification;

import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.enums.shop.ProductPeriod;
import dev.parhamziaei.teahub.enums.shop.ProductType;
import org.springframework.data.jpa.domain.Specification;

public class BillableProductSpecification {

    public static Specification<BillableProduct> byType(ProductType productType) {
        return (root, query, cb) -> cb.equal(root.get("productType"), productType);
    }

    public static Specification<BillableProduct> byPeriod(ProductPeriod productPeriod) {
        return (root, query, cb) -> {
            if (productPeriod == null) return null;
            return cb.equal(root.get("period"), productPeriod);
        };
    }


}
