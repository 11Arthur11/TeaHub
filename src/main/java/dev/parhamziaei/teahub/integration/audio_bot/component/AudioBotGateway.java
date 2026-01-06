package dev.parhamziaei.teahub.integration.audio_bot.component;

import dev.parhamziaei.teahub.entity.jpa.audio_bot.AudioBotNode;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.AudioBotUri;
import dev.parhamziaei.teahub.integration.audio_bot.exception.AudioBotExecutionException;
import dev.parhamziaei.teahub.repository.jpa.AudioBotNodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class AudioBotGateway {

    private final AudioBotNodeRepository audioBotNodeRepository;
    private final Map<String, RestClient> clients = new ConcurrentHashMap<>();

    public AudioBotGateway(
            AudioBotNodeRepository audioBotNodeRepository
    ) {
        this.audioBotNodeRepository = audioBotNodeRepository;
    }

    private RestClient buildRestClient(AudioBotNode audioBotNode) {
        String userPass = audioBotNode.getUsername() + ":" + audioBotNode.getPassword();
        return RestClient.builder()
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
                    httpHeaders.add("Authorization", "Basic " + Base64.getEncoder().encodeToString(userPass.getBytes(StandardCharsets.UTF_8)));
                    httpHeaders.add(HttpHeaders.ACCEPT_CHARSET, "utf-8");
                })
                .baseUrl(audioBotNode.getWebAddress())
                .build();
    }

    @EventListener(ApplicationReadyEvent.class)
    private void initializeClientMap() {
        audioBotNodeRepository.findAll()
                .forEach(node -> clients.put(node.getWebAddress(), buildRestClient(node)));
    }

    private RestClient getClient(AudioBotNode audioBotNode) {
        RestClient restClient = clients.get(audioBotNode.getWebAddress());
        if (restClient == null) {
            restClient = buildRestClient(audioBotNode);
            clients.remove(audioBotNode.getWebAddress());
            clients.put(audioBotNode.getWebAddress(), restClient);
        }
        return restClient;
    }

    public boolean testConnection(AudioBotNode audioBotNode) {
        try {
            RestClient restClient = getClient(audioBotNode);

            AudioBotUri uri = AudioBotUri.builder()
                    .system()
                    .info()
                    .build();

            ResponseEntity<Void> response = restClient.get()
                    .uri(uri.value())
                    .retrieve()
                    .toBodilessEntity();

            return switch (response.getStatusCode().value()) {
                case 200 -> true;
                case 403 -> {
                    log.warn("The music bot API with web-address {} responded with status 403 forbidden", audioBotNode.getWebAddress());
                    yield false;
                }
                default -> {
                    log.warn(
                            "The music bot API with web-address {} responded with {} status code, provision failed with this node",
                            audioBotNode.getWebAddress(),
                            response.getStatusCode()
                    );
                    yield false;
                }
            };
        } catch (Exception ex) {
            log.warn("The music bot API request to web-address {} failed: {}",audioBotNode.getWebAddress(), ex.getMessage(), ex);
            return false;
        }
    }

    private void executeRequest(AudioBotNode audioBotNode, AudioBotUri uri) {
        try {
            ResponseEntity<String> response = getClient(audioBotNode).get()
                    .uri(uri.value())
                    .retrieve()
                    .toEntity(String.class);

            switch (response.getStatusCode().value()) {
                case 200, 204 -> log.info("MusicBot-Gateway -> Command ({}{}) executed successfully", audioBotNode.getWebAddress(), uri);
                default -> {
                    log.warn("MusicBot-Gateway -> Command ({}{}) executed but responded with ({}) status code, body: {}",
                        audioBotNode.getWebAddress(),
                        uri,
                        response.getStatusCode(),
                        response.getBody()
                    );
                    throw new AudioBotExecutionException(response.getBody());
                }
            }
        } catch (Exception ex) {
            log.warn("The music bot API request to web-address {} failed: {}",audioBotNode.getWebAddress(), ex.getMessage(), ex);
        }

    }

    public void createInstance(AudioBotNode audioBotNode, String identifier) {
        AudioBotUri uri = AudioBotUri.builder()
                .setting()
                .create(identifier)
                .build();
        executeRequest(audioBotNode, uri);
    }

}
