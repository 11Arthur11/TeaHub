package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeaSpeakResourceRepository extends JpaRepository<TeaSpeakResource, Long> {
}
