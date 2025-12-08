package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillableProductRepository extends JpaRepository<BillableProduct, Long> {
}
