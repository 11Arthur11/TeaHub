package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.dto.response.dashboard.admin.AdminMetric;
import dev.parhamziaei.teahub.entity.jpa.audio_bot.AudioBotNode;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Comparator;
import java.util.List;

public interface AudioBotNodeRepository extends JpaRepository<AudioBotNode, Long> {

    @Transactional
    default List<AudioBotNode> findProvisionCandidates() {
        return findAll()
                .stream()
                .filter(AudioBotNode::isAvailable)
                .toList();
    }

    @Transactional
    default boolean isAnyProvisionCandidateAvailable() {
        return !findProvisionCandidates().isEmpty();
    }

    boolean existsByWebAddress(String webAddress);


    Long countByEnabled(boolean enabled);
}
