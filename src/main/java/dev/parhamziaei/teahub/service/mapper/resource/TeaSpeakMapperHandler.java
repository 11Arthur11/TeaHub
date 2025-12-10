package dev.parhamziaei.teahub.service.mapper.resource;

import dev.parhamziaei.teahub.dto.response.resource.BaseResourceDetailResponse;
import dev.parhamziaei.teahub.dto.response.resource.teaspeak.TeaSpeakResourceTokenResponse;
import dev.parhamziaei.teahub.dto.response.resource.teaspeak.user.TeaSpeakResourceDetailResponse;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.enums.ResourceType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.repository.jpa.TeaSpeakResourceRepository;
import dev.parhamziaei.teahub.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeaSpeakMapperHandler implements ResourceMapperHandler {

    private final ModelMapper modelMapper;
    private final MessageService messageService;

    @Override
    public ResourceType getType() {
        return ResourceType.TEASPEAK_RESOURCE;
    }

    @Override
    public TeaSpeakResourceDetailResponse map(BillableResource resource) {
        TeaSpeakResource teaSpeakResource = (TeaSpeakResource) resource;
        TeaSpeakResourceDetailResponse dto = modelMapper.map(teaSpeakResource, TeaSpeakResourceDetailResponse.class);
        dto.setStatus(messageService.get(resource.getStatus()));
        dto.setProductName(resource.getProduct().getProductName());
        dto.setPrivilegeToken(modelMapper.map(teaSpeakResource.getPrivilegeTokens(), TeaSpeakResourceTokenResponse.class));
        return dto;
    }

}
