package dev.parhamziaei.teahub.integration.audio_bot.component;

import dev.parhamziaei.teahub.entity.jpa.audio_bot.AudioBotNode;
import dev.parhamziaei.teahub.entity.jpa.resource.AudioBotResource;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.AudioBotUri;
import dev.parhamziaei.teahub.integration.audio_bot.dto.AudioBotInstanceListResponse;
import dev.parhamziaei.teahub.integration.audio_bot.exception.AudioBotHttpException;
import dev.parhamziaei.teahub.repository.jpa.AudioBotNodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.List;
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
        return RestClient.builder()
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
                    httpHeaders.setBasicAuth(
                            audioBotNode.getUsername(),
                            audioBotNode.getPassword(),
                            StandardCharsets.UTF_8
                    );
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
        return clients.computeIfAbsent(
                audioBotNode.getWebAddress(),
                key -> buildRestClient(audioBotNode)
        );
    }

    public Integer testApi(AudioBotNode audioBotNode) {
        RestClient restClient = getClient(audioBotNode);

        AudioBotUri uri = AudioBotUri.builder()
                .system()
                .info()
                .build();

        ResponseEntity<Void> response = restClient.get()
                .uri(uri.value())
                .retrieve()
                .toBodilessEntity();

        return response.getStatusCode().value();
    }

    public boolean testConnection(AudioBotNode audioBotNode) {
        AudioBotUri uri = AudioBotUri.builder()
                .system()
                .info()
                .build();

        try {
            RestClient restClient = getClient(audioBotNode);

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

    public void checkResponse(ResponseEntity<?> response, AudioBotNode audioBotNode, AudioBotUri uri) {
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("MusicBot-Gateway -> Command ({}{}) executed successfully", audioBotNode.getWebAddress(), uri.value());
        } else {
            log.warn("MusicBot-Gateway -> Command ({}{}) executed but responded with ({}) status code, body: {}",
                    audioBotNode.getWebAddress(),
                    uri,
                    response.getStatusCode(),
                    response.getBody()
            );
            throw new AudioBotHttpException("STATUS:" + response.getStatusCode());
        }
    }

    private void execute(AudioBotNode audioBotNode, AudioBotUri uri) {
        try {
            ResponseEntity<String> response = getClient(audioBotNode).get()
                    .uri(uri.value())
                    .retrieve()
                    .toEntity(String.class);

            checkResponse(response, audioBotNode, uri);
        } catch (Exception ex) {
            log.error("{}{}", audioBotNode.getWebAddress(), uri.value());
            log.warn("The music bot API request to web-address {} failed: {}",audioBotNode.getWebAddress(), ex.getMessage(), ex);
        }
    }

    public void connectInstance(AudioBotNode audioBotNode, String identifier) {
        AudioBotUri connectUri = AudioBotUri.builder()
                .bot()
                .connect(identifier)
                .build();

        execute(audioBotNode, connectUri);
    }

    public void createInstance(AudioBotNode audioBotNode, String identifier) {
        AudioBotUri createUri = AudioBotUri.builder()
                .settings()
                .create(identifier)
                .build();
        execute(audioBotNode, createUri);
    }

    public void setInstanceConnectAddress(AudioBotResource resource, String address) {
        AudioBotUri setConnect = AudioBotUri.builder()
                .settings()
                .bot()
                .set(resource.getIdentifier().toString())
                .connectAddress(address)
                .build();

        execute(resource.getParentNode(), setConnect);
    }

    public void setInstanceConnectNickname(AudioBotResource resource, String nickname) {
        final AudioBotUri setNickname = AudioBotUri.builder()
                .settings()
                .bot()
                .set(resource.getIdentifier().toString())
                .connectNickname(nickname)
                .build();

        execute(resource.getParentNode(), setNickname);
    }

    public void setInstanceConnectNickname(AudioBotResource resource, Long botId, String nickname) {
        final AudioBotUri setNickname = AudioBotUri.builder()
                .bot()
                .use(botId)
                .setConnectName(nickname)
                .build();

        execute(resource.getParentNode(), setNickname);
    }


    public List<AudioBotInstanceListResponse> getInstanceList(AudioBotNode audioBotNode) {
        AudioBotUri getListUri = AudioBotUri.builder()
                .bot()
                .list()
                .build();

        ResponseEntity<List<AudioBotInstanceListResponse>> responseList = getClient(audioBotNode).get()
                .uri(getListUri.value())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {});

        checkResponse(responseList, audioBotNode, getListUri);
        return responseList.getBody();
    }

    public void setInstanceConnectPassword(AudioBotResource resource, String password) {
        AudioBotUri setPassword = AudioBotUri.builder()
                .settings()
                .bot()
                .set(resource.getIdentifier().toString())
                .connectPassword(password)
                .build();

        execute(resource.getParentNode(), setPassword);
    }

}
