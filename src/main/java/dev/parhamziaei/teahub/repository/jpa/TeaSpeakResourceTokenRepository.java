package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResourceToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeaSpeakResourceTokenRepository extends JpaRepository<TeaSpeakResourceToken, Long> {
}
