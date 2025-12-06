package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TeaSpeakProductRepository extends JpaSpecificationExecutor<TeaSpeakProduct>, JpaRepository<TeaSpeakProduct, Long> {


}
