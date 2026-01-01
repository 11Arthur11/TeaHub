package dev.parhamziaei.teahub.integration.audio_bot.component;

import dev.parhamziaei.teahub.entity.jpa.audio_bot.AudioBotNode;
import dev.parhamziaei.teahub.entity.jpa.resource.AudioBotResource;
import dev.parhamziaei.teahub.integration.audio_bot.internal_service.AudioBotProvisionStrategyFactory;
import dev.parhamziaei.teahub.integration.audio_bot.internal_service.AudioBotProvisionStrategyHandler;
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

    @Autowired
    public AudioBotNodeManager(
            AudioBotProvisionStrategyFactory strategyFactory
    ) {
        this.strategyHandler = strategyFactory.getStrategy();
    }

    private RestClient buildRestClient(AudioBotNode audioBotNode) {
        String userPass = audioBotNode.getUsername() + ":" + audioBotNode.getPassword();
        return RestClient.builder()
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
                    httpHeaders.add("Authorization", Base64.getEncoder().encodeToString(userPass.getBytes(StandardCharsets.UTF_8)));
                    httpHeaders.add(HttpHeaders.ACCEPT_CHARSET, "utf-8");
                })
                .baseUrl(audioBotNode.getWebAddress())
                .build();
    }

    private AudioBotNode getAvailableBotNode() {
        AudioBotNode provider = strategyHandler.getProviderNode();
        log.info("Provision-Operation -> Selected audio-bot node is (ID={} - HOST={}) by {} Strategy",
                provider.getId(),
                provider.getWebAddress(),
                strategyHandler.getType().name()
        );
        return provider;
    }

    @Transactional
    protected String generateKey(AudioBotResource resource) {
        return "RID:" + resource.getId() + "_UID:" + resource.getOwner().getId() + "_" + UUID.randomUUID();
    }

}
