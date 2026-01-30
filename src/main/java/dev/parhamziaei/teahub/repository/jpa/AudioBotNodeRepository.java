package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.audio_bot.AudioBotNode;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

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

}
