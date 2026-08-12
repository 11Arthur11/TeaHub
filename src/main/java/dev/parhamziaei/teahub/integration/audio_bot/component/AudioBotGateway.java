package dev.parhamziaei.teahub.integration.audio_bot.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.parhamziaei.teahub.dto.request.query.BasePaginationRequest;
import dev.parhamziaei.teahub.entity.jpa.audio_bot.AudioBotNode;
import dev.parhamziaei.teahub.entity.jpa.resource.AudioBotResource;
import dev.parhamziaei.teahub.enums.audio_bot.AudioBotStatus;
import dev.parhamziaei.teahub.exception.custom.service.audio_bot.AudioBotGatewayException;
import dev.parhamziaei.teahub.integration.audio_bot.component.dsl.AudioBotUri;
import dev.parhamziaei.teahub.integration.audio_bot.dto.ABApiTokenResponse;
import dev.parhamziaei.teahub.integration.audio_bot.dto.ABConnectSettingsResponse;
import dev.parhamziaei.teahub.integration.audio_bot.dto.mixin.*;
import dev.parhamziaei.teahub.integration.audio_bot.dto.playlist.ABPlayListDetailResponse;
import dev.parhamziaei.teahub.integration.audio_bot.dto.playlist.ABPlayListItemResponse;
import dev.parhamziaei.teahub.integration.audio_bot.dto.playlist.ABPlayListsResponse;
import dev.parhamziaei.teahub.integration.audio_bot.dto.ABInstanceListResponse;
import dev.parhamziaei.teahub.integration.audio_bot.dto.ABInstanceSettingsResponse;
import dev.parhamziaei.teahub.integration.audio_bot.exception.AudioBotHttpException;
import dev.parhamziaei.teahub.integration.audio_bot.exception.AudioBotScopedPanelNotConfiguredException;
import dev.parhamziaei.teahub.repository.jpa.AudioBotNodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AudioBotGateway {

    private final AudioBotNodeRepository audioBotNodeRepository;
    private final Map<String, RestClient> clients = new ConcurrentHashMap<>();

    private RestClient buildRestClient(AudioBotNode audioBotNode) {
        SimpleModule module = new SimpleModule();

        module.addSerializer(AudioBotStatus.class,
                new JsonSerializer<>() {
                    @Override
                    public void serialize(
                            AudioBotStatus value,
                            JsonGenerator gen,
                            SerializerProvider serializers
                    ) throws IOException {
                        gen.writeNumber(value.code());
                    }
                });

        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        mapper.registerModule(module);
        mapper.addMixIn(ABConnectSettingsResponse.class, ABConnectSettingsResponseMixin.class);
        mapper.addMixIn(ABPlayListDetailResponse.class, ABPlayListDetailResponseMixin.class);
        mapper.addMixIn(ABPlayListItemResponse.class, ABPlayListItemResponseMixin.class);
        mapper.addMixIn(ABPlayListsResponse.class, ABPlayListsResponseMixin.class);
        mapper.addMixIn(ABInstanceListResponse.class, ABInstanceListResponseMixin.class);
        mapper.addMixIn(ABApiTokenResponse.class, ABApiTokenResponseMixin.class);
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
                .messageConverters(List.of(new MappingJackson2HttpMessageConverter(mapper)))
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

    public Integer probeNodeHealth(AudioBotNode audioBotNode) {
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
            log.info("MusicBot-Gateway -> Command [{}{}] executed successfully", audioBotNode.getWebAddress(), uri.value());
            if (response.getBody() != null)
                log.debug(response.getBody().toString());
        } else {
            log.warn("MusicBot-Gateway -> Command [{}{}] sent but responded with ({}) status code, body: {}",
                    audioBotNode.getWebAddress(),
                    uri,
                    response.getStatusCode(),
                    response.getBody()
            );
            if (response.getBody() != null)
                log.debug(response.getBody().toString());
            throw new AudioBotHttpException("STATUS:" + response.getStatusCode());
        }
    }

    private void execute(AudioBotNode audioBotNode, AudioBotUri uri) {
        try {
            ResponseEntity<Void> response = getClient(audioBotNode).get()
                    .uri(URI.create(uri.value()))
                    .retrieve()
                    .toBodilessEntity();

            checkResponse(response, audioBotNode, uri);
        } catch (Exception ex) {
            log.error("Failed to execute ({}{})", audioBotNode.getWebAddress(), uri.value(), ex);
            throw new AudioBotGatewayException(ex.getMessage());
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

    public void disconnectInstance(AudioBotResource audioBotNode, Long botId) {
        final AudioBotUri disconnectUri = AudioBotUri.builder()
                .bot()
                .use(botId)
                .disconnect()
                .build();

        execute(audioBotNode.getParentNode(), disconnectUri);
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

    public void updateInstanceNickname(AudioBotResource resource, Long botId, String nickname) {
        final AudioBotUri setNickname = AudioBotUri.builder()
                .bot()
                .use(botId)
                .updateNickname(nickname)
                .build();

        execute(resource.getParentNode(), setNickname);
    }

    public List<ABInstanceListResponse> getInstanceList(AudioBotNode audioBotNode) {
        AudioBotUri getListUri = AudioBotUri.builder()
                .bot()
                .list()
                .build();

        ResponseEntity<List<ABInstanceListResponse>> responseList = getClient(audioBotNode).get()
                .uri(getListUri.value())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {});

        checkResponse(responseList, audioBotNode, getListUri);
        return responseList.getBody();
    }

    public ABInstanceSettingsResponse getInstanceSettings(AudioBotResource resource) {
        AudioBotUri getUri = AudioBotUri.builder()
                .settings()
                .bot()
                .get(resource.getIdentifier().toString())
                .build();

        ResponseEntity<ABInstanceSettingsResponse> response = getClient(resource.getParentNode()).get()
                .uri(getUri.value())
                .retrieve()
                .toEntity(ABInstanceSettingsResponse.class);

        checkResponse(response, resource.getParentNode(), getUri);
        return response.getBody();
    }

    public void setConnectOnRuntime(AudioBotResource resource, boolean connectOnRuntime) {
        final AudioBotUri setConnectOnRuntimeUri = AudioBotUri.builder()
                .settings()
                .bot()
                .set(resource.getIdentifier().toString())
                .connectOnRuntime(connectOnRuntime)
                .build();

        execute(resource.getParentNode(), setConnectOnRuntimeUri);
    }

//    public void createNewPlaylist(AudioBotResource resource, Long botId, String fileName, String playlistName) {
//        final AudioBotUri createNewPlaylistUri = AudioBotUri.builder()
//                .bot()
//                .use(botId)
//                .playlist()
//                .create(fileName, playlistName)
//                .build();
//
//        execute(resource.getParentNode(), createNewPlaylistUri);
//    }

    public List<ABPlayListsResponse> getInstancePlayLists(AudioBotResource resource, Long botId) {
        final AudioBotUri getPlaylistsUri = AudioBotUri.builder()
                .bot()
                .use(botId)
                .playlist()
                .list()
                .build();

        ResponseEntity<List<ABPlayListsResponse>> playlistsResponse = getClient(resource.getParentNode()).get()
                .uri(getPlaylistsUri.value())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {});

        checkResponse(playlistsResponse, resource.getParentNode(), getPlaylistsUri);
        return playlistsResponse.getBody();
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

    public void createPlaylist(AudioBotResource resource, Long botId, String playlistName) {
        final AudioBotUri createPlaylistUri = AudioBotUri.builder()
                .bot()
                .use(botId)
                .playlist()
                .create(UUID.randomUUID().toString(), playlistName)
                .build();

        execute(resource.getParentNode(), createPlaylistUri);
    }

    public void deletePlaylist(AudioBotResource resource, Long botId, String playlistFilename) {
        final AudioBotUri deletePlaylistUri = AudioBotUri.builder()
                .bot()
                .use(botId)
                .playlist()
                .delete(playlistFilename)
                .build();

        execute(resource.getParentNode(), deletePlaylistUri);
    }

    public void addTrackToPlaylist(AudioBotResource resource, Long botId, String playlistFilename, String trackLink) {
        final AudioBotUri addTrackUri = AudioBotUri.builder()
                .bot()
                .use(botId)
                .playlist()
                .itemAdd(playlistFilename, trackLink)
                .build();

        execute(resource.getParentNode(), addTrackUri);
    }

    public void deleteTrackFromPlaylist(
            AudioBotResource resource,
            Long botId,
            String playlistFilename,
            Integer trackIndex
    ) {
        final AudioBotUri deleteTrackUri = AudioBotUri.builder()
                .bot()
                .use(botId)
                .playlist()
                .itemDelete(playlistFilename, trackIndex)
                .build();

        execute(resource.getParentNode(), deleteTrackUri);
    }

    public ABApiTokenResponse getToken(AudioBotResource resource) {
        AudioBotUri uri = new AudioBotUri(
                "/api/api/token/bot/create/" + resource.getIdentifier().toString()
                + "/" + formatRemainingTime(resource.getExpiration())
        );

        try {
            ResponseEntity<ABApiTokenResponse> tokenResponse = getClient(resource.getParentNode()).get()
                    .uri(uri.value())
                    .retrieve()
                    .toEntity(ABApiTokenResponse.class);

            checkResponse(tokenResponse, resource.getParentNode(), uri);
            return tokenResponse.getBody();
        } catch (HttpClientErrorException.UnprocessableEntity ignored) {
            throw new AudioBotScopedPanelNotConfiguredException("This Version of AudioBot is not supported for generating scoped token, Use TeaCloud Fork");
        }
    }

    public void changeBotSuspendState(AudioBotResource resource, boolean suspend) {
        AudioBotUri uri = new AudioBotUri(
                "/api/api/token/bot/suspend/" + resource.getIdentifier().toString()
                + "/" + suspend
        );

        try {
            ResponseEntity<Void> tokenResponse = getClient(resource.getParentNode()).get()
                    .uri(uri.value())
                    .retrieve()
                    .toBodilessEntity();

            checkResponse(tokenResponse, resource.getParentNode(), uri);
        } catch (HttpClientErrorException.UnprocessableEntity ignored) {
            throw new AudioBotScopedPanelNotConfiguredException("This Version of AudioBot is not supported for managing scoped token, Use TeaCloud Fork");
        }

    }

    public void deleteInstance(AudioBotResource resource) {
        AudioBotUri uri = AudioBotUri.builder()
                .settings()
                .delete(resource.getIdentifier().toString())
                .build();

        execute(resource.getParentNode(), uri);
    }

    public ABPlayListDetailResponse getPlaylistDetail(AudioBotResource resource, Long botId, String playlistFilename, BasePaginationRequest paginationRequest) {
        final AudioBotUri getPlaylistsUri = AudioBotUri.builder()
                .bot()
                .use(botId)
                .playlist()
                .show(
                        playlistFilename,
                        paginationRequest.getPage(),
                        paginationRequest.getSize()
                )
                .build();

        ResponseEntity<ABPlayListDetailResponse> playlistsResponse = getClient(resource.getParentNode()).get()
                .uri(getPlaylistsUri.value())
                .retrieve()
                .toEntity(ABPlayListDetailResponse.class);

        checkResponse(playlistsResponse, resource.getParentNode(), getPlaylistsUri);

        ABPlayListDetailResponse playlistDetail = playlistsResponse.getBody();

        if (playlistDetail != null && playlistDetail.getPlayListItems() != null) {
            for (int i=0; i<playlistDetail.getPlayListItems().size(); i++) {
                playlistDetail.getPlayListItems().get(i).setIndex(i);
            }
        }

        return playlistsResponse.getBody();
    }

    public void playTheList(AudioBotResource resource, Long botId, String playlistFilename) {
        final AudioBotUri playTheListUri = AudioBotUri.builder()
                .bot()
                .use(botId)
                .playlist()
                .play(playlistFilename)
                .build();

        execute(resource.getParentNode(), playTheListUri);
    }

    private static String formatRemainingTime(LocalDateTime futureTime) {
        Duration duration = Duration.between(LocalDateTime.now(), futureTime);

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();

        if (hours > 0 && minutes > 0) {
            return hours + "h" + minutes + "m";
        }

        if (hours > 0) {
            return hours + "h";
        }

        return minutes + "m";
    }

}
