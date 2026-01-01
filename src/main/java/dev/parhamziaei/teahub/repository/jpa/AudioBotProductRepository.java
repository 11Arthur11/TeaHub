package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.shop.AudioBotProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AudioBotProductRepository extends JpaSpecificationExecutor<AudioBotProduct>, JpaRepository<AudioBotProduct, Long> {
}
