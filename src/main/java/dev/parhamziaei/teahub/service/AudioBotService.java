package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.audio_bot.admin.AudioBotNodeInitRequest;
import dev.parhamziaei.teahub.dto.response.audio_bot.admin.AudioBotNodeDetailResponse;
import dev.parhamziaei.teahub.dto.response.audio_bot.admin.AudioBotNodeListResponse;
import dev.parhamziaei.teahub.entity.jpa.audio_bot.AudioBotNode;
import dev.parhamziaei.teahub.entity.jpa.resource.AudioBotResource;
import dev.parhamziaei.teahub.entity.jpa.shop.AudioBotProduct;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.audio_bot.AudioBotAlreadyInitiatedException;
import dev.parhamziaei.teahub.integration.audio_bot.component.AudioBotGateway;
import dev.parhamziaei.teahub.integration.audio_bot.component.AudioBotNodeManager;
import dev.parhamziaei.teahub.integration.audio_bot.dto.AudioBotInstanceListResponse;
import dev.parhamziaei.teahub.kafka.event.resource.AudioBotDeployEvent;
import dev.parhamziaei.teahub.repository.jpa.AudioBotNodeRepository;
import dev.parhamziaei.teahub.repository.jpa.AudioBotProductRepository;
import dev.parhamziaei.teahub.repository.jpa.AudioBotResourceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AudioBotService {

    private final AudioBotGateway audioBotGateway;
    private final AudioBotNodeManager audioBotNodeManager;
    private final AudioBotProductRepository audioBotProductRepository;
    private final AudioBotNodeRepository audioBotNodeRepository;
    private final AudioBotResourceRepository audioBotResourceRepository;

    @Transactional
    public void deployInstance(AudioBotDeployEvent event) {
        AudioBotProduct product = audioBotProductRepository.findById(event.getProductId())
                .orElseThrow(() -> new NoSuchEntityException("Product not found"));

        AudioBotResource resource = audioBotResourceRepository.findById(event.getResourceId())
                .orElseThrow(() -> new NoSuchEntityException("Resource not found"));

        final AudioBotNode node;
        if (product.hasCustomProvider()) {
            node = audioBotNodeRepository.findById(product.getProviderNodeId())
                    .orElseThrow(() -> new NoSuchEntityException("Provider node not found with id " + product.getProviderNodeId()));
        } else {
            node = audioBotNodeManager.getAvailableBotNode();
        }

        UUID identifier = UUID.randomUUID();

        resource.setIdentifier(identifier);

        audioBotGateway.createInstance(node, identifier.toString());
        resource.setParentNode(node);
        audioBotGateway.setInstanceConnectAddress(resource, event.getResourceRequest().getServerAddress());
        if (event.getResourceRequest().getServerPassword() != null)
            audioBotGateway.setInstanceConnectPassword(resource, event.getResourceRequest().getServerPassword());
        if (event.getResourceRequest().getBotNickname() != null)
            audioBotGateway.setInstanceConnectNickname(resource, event.getResourceRequest().getBotNickname());
        audioBotGateway.connectInstance(node, identifier.toString());
        syncResource(resource);
    }

    public void syncResource(AudioBotResource resource) {
        audioBotGateway.getInstanceList(resource.getParentNode())
                .stream()
                .filter(a -> a.getName().equals(resource.getIdentifier().toString()))
                .findFirst()
                .ifPresent(i -> resource.setBotStatus(i.getStatus()));
    }



}
