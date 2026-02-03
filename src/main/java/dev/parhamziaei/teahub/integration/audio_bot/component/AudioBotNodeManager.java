package dev.parhamziaei.teahub.integration.audio_bot.component;

import dev.parhamziaei.teahub.entity.jpa.audio_bot.AudioBotNode;
import dev.parhamziaei.teahub.entity.jpa.resource.AudioBotResource;
import dev.parhamziaei.teahub.enums.audio_bot.AudioBotStatus;
import dev.parhamziaei.teahub.enums.audio_bot.NodeStatus;
import dev.parhamziaei.teahub.integration.audio_bot.internal_service.AudioBotProvisionStrategyFactory;
import dev.parhamziaei.teahub.integration.audio_bot.internal_service.AudioBotProvisionStrategyHandler;
import dev.parhamziaei.teahub.repository.jpa.AudioBotNodeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AudioBotNodeManager {

    private final AudioBotProvisionStrategyHandler strategyHandler;
    private final AudioBotNodeRepository audioBotNodeRepo;
    private final AudioBotGateway audioBotGateway;

    @Autowired
    public AudioBotNodeManager(
            AudioBotProvisionStrategyFactory strategyFactory, AudioBotNodeRepository audioBotNodeRepo, AudioBotGateway audioBotGateway
    ) {
        this.strategyHandler = strategyFactory.getStrategy();
        this.audioBotNodeRepo = audioBotNodeRepo;
        this.audioBotGateway = audioBotGateway;
    }

    public AudioBotNode getAvailableBotNode() {
        AudioBotNode provider = strategyHandler.getProviderNode();
        log.info("Provision-Operation -> Selected audio-bot node is (ID={} - HOST={}) by {} Strategy",
                provider.getId(),
                provider.getWebAddress(),
                strategyHandler.getType().name()
        );
        return provider;
    }

    public NodeStatus calculateNodeStatus(AudioBotNode audioBotNode) {
        if (!audioBotNode.isEnabled())
            return NodeStatus.DISABLED;
        switch (audioBotGateway.testApi(audioBotNode)) {
            case 200 -> {
                return NodeStatus.DISPATCHED;
            }
            case 403 -> {
                return NodeStatus.LOGIN_FAILED;
            }
            default -> {
                return NodeStatus.UNREACHABLE;
            }
        }
    }

}
