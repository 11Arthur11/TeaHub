package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.resource.AudioBotResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AudioBotResourceRepository extends JpaSpecificationExecutor<AudioBotResource>, JpaRepository<AudioBotResource, Long> {
}
