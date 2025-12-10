package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TeaSpeakResourceRepository extends JpaSpecificationExecutor<TeaSpeakResource>, JpaRepository<TeaSpeakResource, Long> {
}
