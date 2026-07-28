package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.audio_bot.admin.AudioBotNodeEditRequest;
import dev.parhamziaei.teahub.dto.request.audio_bot.admin.AudioBotNodeInitRequest;
import dev.parhamziaei.teahub.dto.response.audio_bot.admin.AudioBotNodeDetailResponse;
import dev.parhamziaei.teahub.dto.response.audio_bot.admin.AudioBotNodeListResponse;
import dev.parhamziaei.teahub.dto.response.dashboard.admin.AdminMetric;
import dev.parhamziaei.teahub.dto.response.dashboard.admin.CountSummary;
import dev.parhamziaei.teahub.entity.jpa.ResourceProvisioningStrategy;
import dev.parhamziaei.teahub.entity.jpa.audio_bot.AudioBotNode;
import dev.parhamziaei.teahub.enums.audio_bot.AudioBotStatus;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.audio_bot.AudioBotAlreadyInitiatedException;
import dev.parhamziaei.teahub.exception.custom.service.audio_bot.AudioBotNodeHasActiveInstanceException;
import dev.parhamziaei.teahub.integration.audio_bot.component.AudioBotGateway;
import dev.parhamziaei.teahub.integration.audio_bot.component.AudioBotNodeManager;
import dev.parhamziaei.teahub.integration.audio_bot.dto.ABInstanceListResponse;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import dev.parhamziaei.teahub.repository.jpa.AudioBotNodeRepository;
import dev.parhamziaei.teahub.repository.jpa.ResourceProvisioningStrategyRepository;
import dev.parhamziaei.teahub.service.mapper.AudioBotMapStruct;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AudioBotNodeService {

    private final AudioBotNodeRepository audioBotNodeRepository;
    private final ModelMapper modelMapper;
    private final AudioBotGateway audioBotGateway;
    private final AudioBotNodeManager audioBotNodeManager;
    private final AudioBotMapStruct audioBotMapStruct;
    private final ResourceProvisioningStrategyRepository provisioningStrategyRepo;

    public void initiateNode(AudioBotNodeInitRequest nodeInitRequest) {
        if (audioBotNodeRepository.existsByWebAddress(nodeInitRequest.getWebAddress()))
            throw new AudioBotAlreadyInitiatedException(nodeInitRequest.getWebAddress());

        AudioBotNode audioBotNode = AudioBotNode.builder()
                .name(nodeInitRequest.getName())
                .webAddress(nodeInitRequest.getWebAddress())
                .username(nodeInitRequest.getUsername())
                .password(nodeInitRequest.getPassword())
                .maxBotInstance(nodeInitRequest.getMaxBotInstance())
                .enabled(nodeInitRequest.isEnabled())
                .build();

        audioBotNode.setNodeStatus(audioBotNodeManager.calculateNodeStatus(audioBotNode));
        audioBotNodeRepository.save(audioBotNode);
    }

    public void changeProvisioningStrategy(ProvisionStrategy newStrategy) {
        provisioningStrategyRepo.save(new ResourceProvisioningStrategy(ResourceType.AUDIO_BOT, newStrategy));
    }

    public AdminMetric.NodeMetric getNodeMetric() {
        return new AdminMetric.NodeMetric(
                new CountSummary(
                        audioBotNodeRepository.count(),
                        audioBotNodeRepository.countByEnabled(true)
                ),
                provisioningStrategyRepo.getAudioBotStrategy()
        );
    }

    public ProvisionStrategy getProvisionStrategy() {
        return provisioningStrategyRepo.getAudioBotStrategy();
    }

    public List<AudioBotNodeListResponse> getNodeList() {
        List<AudioBotNode> audioBotNodes = audioBotNodeRepository.findAll()
                .stream()
                .peek(audioBotNode -> audioBotNode.setNodeStatus(audioBotNodeManager.calculateNodeStatus(audioBotNode)))
                .toList();

        List<AudioBotNodeListResponse> nodeListResponses = audioBotNodes.stream()
                .map(audioBotNode -> modelMapper.map(audioBotNode, AudioBotNodeListResponse.class))
                .toList();

        if (nodeListResponses.isEmpty())
            throw new NoSuchDataException();

        return nodeListResponses;
    }

    public AudioBotNodeDetailResponse getNodeDetail(Long id) {
        AudioBotNode node =  audioBotNodeRepository.findById(id)
                .orElseThrow(NoSuchEntityException::new);

        node.setNodeStatus(audioBotNodeManager.calculateNodeStatus(node));

        List<ABInstanceListResponse> instanceList = audioBotGateway.getInstanceList(node);

        AudioBotNodeDetailResponse nodeResponse = modelMapper.map(node, AudioBotNodeDetailResponse.class);
        nodeResponse.setAllInstanceCount(instanceList.size());
        nodeResponse.setOnlineInstanceCount(
                instanceList.stream()
                        .filter(i -> i.getStatus().equals(AudioBotStatus.CONNECTED))
                        .toList()
                        .size()
                );

        return nodeResponse;
    }

    public void editNode(Long nodeId, AudioBotNodeEditRequest editRequest) {
        AudioBotNode node = audioBotNodeRepository.findById(nodeId)
                .orElseThrow(NoSuchEntityException::new);

        audioBotMapStruct.toEntity(editRequest, node);
        node.setNodeStatus(audioBotNodeManager.calculateNodeStatus(node));
        audioBotNodeRepository.save(node);
    }

    public void deleteNode(Long nodeId) {
        AudioBotNode node = audioBotNodeRepository.findById(nodeId)
                .orElseThrow(NoSuchEntityException::new);

        Hibernate.initialize(node.getInstances());
        if (node.getInstances().isEmpty())
            audioBotNodeRepository.delete(node);
        else
            throw new AudioBotNodeHasActiveInstanceException();
    }

}
