package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.audio_bot.AudioBotNode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioBotNodeRepository extends JpaRepository<AudioBotNode, Long> {
}
