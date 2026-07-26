package dev.parhamziaei.teahub.service.mapper.resource;

import dev.parhamziaei.teahub.dto.response.resource.AbstractResourceDetailResponse;
import dev.parhamziaei.teahub.dto.response.resource.audio_bot.admin.AudioBotResourceDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.resource.audio_bot.user.AudioBotResourceDetailResponse;
import dev.parhamziaei.teahub.entity.jpa.resource.AudioBotResource;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import dev.parhamziaei.teahub.integration.audio_bot.dto.ABConnectSettingsResponse;
import dev.parhamziaei.teahub.service.AudioBotService;
import dev.parhamziaei.teahub.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AudioBotResourceMapper implements ResourceMapperHandler {

    private final AudioBotService audioBotService;
    private final ModelMapper modelMapper;

    @Override
    public ResourceType getType() {
        return ResourceType.AUDIO_BOT;
    }

    private <T extends AudioBotResourceDetailResponse> T mapInternal(AudioBotResource resource, Class<T> clazz) {
        audioBotService.syncWithNode(resource);
        T resourceDetail = modelMapper.map(resource, clazz);
        resourceDetail.setProductName(resource.getProduct().getProductName());

        ABConnectSettingsResponse instanceConnectSettings = audioBotService.getInstanceSetting(resource).getConnect();
        resourceDetail.setBotNickname(instanceConnectSettings.getName());
        resourceDetail.setServerAddress(instanceConnectSettings.getAddress());
        resourceDetail.setServerPassword(instanceConnectSettings.getServerPassword().getPassword());
        resourceDetail.setPeriod(resource.getProduct().getPeriod());
        return resourceDetail;
    }

    @Override
    public AbstractResourceDetailResponse mapResourceDetailResponse(BillableResource resource) {
        AudioBotResource audioBotResource = (AudioBotResource) resource;
        return mapInternal(audioBotResource, AudioBotResourceDetailResponse.class);
    }

    @Override
    public AbstractResourceDetailResponse mapResourceDetailAdminResponse(BillableResource resource) {
        AudioBotResource audioBotResource = (AudioBotResource) resource;
        AudioBotResourceDetailAdminResponse response = mapInternal(audioBotResource, AudioBotResourceDetailAdminResponse.class);
        response.setOwnerId(audioBotResource.getOwner().getId());
        response.setIdentifier(audioBotResource.getIdentifier().toString());
        return response;
    }

}
