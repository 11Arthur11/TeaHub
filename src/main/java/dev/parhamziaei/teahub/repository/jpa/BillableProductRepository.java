package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BillableProductRepository extends JpaSpecificationExecutor<BillableProduct>, JpaRepository<BillableProduct, Long> {
}
