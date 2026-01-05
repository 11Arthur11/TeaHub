package dev.parhamziaei.teahub.integration.audio_bot.component;

import dev.parhamziaei.teahub.entity.jpa.audio_bot.AudioBotNode;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.AudioBotUri;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class AudioBotGateway {

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

    public boolean testConnection(AudioBotNode audioBotNode) {
        try {
            RestClient restClient = buildRestClient(audioBotNode);

            AudioBotUri uri = AudioBotUri.builder()
                    .system()
                    .info()
                    .build();

            ResponseEntity<Void> is = restClient.get()
                    .uri(uri.value())
                    .retrieve()
                    .toBodilessEntity();

            return switch (is.getStatusCode().value()) {
                case 200 -> true;
                case 403 -> {
                    log.warn("The music bot API with web-address {} responded with status 403 forbidden", audioBotNode.getWebAddress());
                    yield false;
                }
                default -> {
                    log.warn(
                            "The music bot API with web-address {} responded with {} status code, provision failed with this node",
                            audioBotNode.getWebAddress(),
                            is.getStatusCode()
                    );
                    yield false;
                }
            };
        } catch (Exception ex) {
            log.warn("The music bot API request to web-address {} failed: {}",audioBotNode.getWebAddress(), ex.getMessage(), ex);
            return false;
        }
    }

    public void createInstance(AudioBotNode audioBotNode) {
        try {
            RestClient restClient = buildRestClient(audioBotNode);
            AudioBotUri uri = AudioBotUri.builder()
                    .setting()
                    .create("T")
                    .build();

            ResponseEntity<Void> is = restClient.get()
                    .uri(uri.value())
                    .retrieve()
                    .toBodilessEntity();

        } catch (Exception ex) {
            log.warn("The music bot API request to web-address {} failed: {}",audioBotNode.getWebAddress(), ex.getMessage(), ex);
        }
    }

}
